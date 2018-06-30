/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object;

import com.biqasoft.audit.object.customfield.CustomObjectUtils;
import com.biqasoft.audit.object.diffs.ChangeObjectDTO;
import com.biqasoft.auth.CurrentUserContextProvider;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.core.objects.CustomObjectData;
import com.biqasoft.microservice.database.MongoTenantHelper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mongodb.MongoClient;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.changelog.SimpleTextChangeLog;
import org.javers.core.diff.Change;
import org.javers.repository.jql.QueryBuilder;
import org.javers.repository.mongo.MongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

/**
 * Get object changes (Object auditing and diff) with javers http://javers.org/
 * <p>
 * Created by Nikita Bakaev, ya@nbakaev.ru on 12/27/2015.
 * All Rights Reserved
 */
@Service
public class ObjectsAuditHistoryService {

    private final MongoClient mongoClient;
    private final MongoTenantHelper mongoTenantHelper;
    private List<BaseClassFinder> classFinders;

    @Autowired
    public ObjectsAuditHistoryService(MongoClient mongoClient, MongoTenantHelper mongoTenantHelper) {
        this.mongoClient = mongoClient;

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("history-javers-thread-%d").build();
        this.executorService = new ScheduledThreadPoolExecutor(15, namedThreadFactory);
        this.mongoTenantHelper = mongoTenantHelper;
    }

    // key - domain
    private final ConcurrentHashMap<String, Javers> javersMongoRepositories = new ConcurrentHashMap<>(30);
    private final ExecutorService executorService;
    private static final Logger logger = LoggerFactory.getLogger(ObjectsAuditHistoryService.class);

    /**
     * Get any {@link BaseClass} object stored in database
     * Work right with custom objects which is stored in separate databases
     *
     * @param classToFind class object to find.
     *                    Need to 1) set object ID
     *                    2) java class
     * @param dbName      mongodb database name
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseClass> T getBiqaClassById(T classToFind, String dbName) {
        Criteria criteria = new Criteria();
        criteria.and("id").is(classToFind.getId());
        Query query = new Query(criteria);

        MongoOperations template = mongoTenantHelper.domainDataBaseUnsafeGet(dbName);

        if (!CollectionUtils.isEmpty(classFinders)) {
            for (BaseClassFinder x : classFinders) {
                if (x.canFind(classToFind.getClass(), dbName)) {
                    T aClass = x.findClass(classToFind, dbName);
                    if (aClass != null) {
                        return aClass;
                    }
                }
            }
        }

        // custom objects stored in separate database - get name manually
        if (classToFind instanceof CustomObjectData) {
            CustomObjectData data = (CustomObjectData) classToFind;
            return (T) template.findOne(query, classToFind.getClass(), CustomObjectUtils.getCollectionNameFromCustomObject(data));
        } else {
            return (T) template.findOne(query, classToFind.getClass());
        }
    }

    public Javers getJaversForCurrentUser(CurrentUserContextProvider currentUser) {
        return getJavers(currentUser.getDomain().getDomain());
    }

    public Javers getJavers(String domain) {

        // if we have connection to domain user db
        // just return it
        if (javersMongoRepositories.containsKey(domain)) return javersMongoRepositories.get(domain);

        // if we have not - create connection
        MongoRepository javersMongoRepository = new MongoRepository(mongoClient.getDatabase(domain));

        Javers javers = JaversBuilder.javers() // http://javers.org/documentation/domain-configuration/ : registerValue(SalesFunnelStatusWithoutDataSource.class)
                .registerJaversRepository(javersMongoRepository)
                .build();

        javersMongoRepositories.put(domain, javers);

        return javers;
    }

    public <T extends BaseClass> void auditChangesForCurrentUser(T t, CurrentUserContextProvider currentUser) {
        auditChanges(currentUser.getDomain().getDomain(), currentUser.getUserAccount().getId(), t);
    }

    /**
     * Call this method AFTER making(saving) changes to database,
     * so we must be able to read already updated or new created object
     * <p>
     * do not use MongoTemplate template directly - we are not in HTTP bounded request scope
     * MongoOperations mongoOperations = mongoTenantHelper.domainDataBaseUnsafeGet(dbName);
     **/
    public <T extends BaseClass> void auditChanges(String dbName, String committerId, T t) {

        // do this async because it can create a lot of database request
        executorService.submit(() -> {
            try {
                // it's safe to all objects including UserAccountDto with sensitive info
                // if we check requested user domain(which is dbName)
                T newClass = getBiqaClassById(t, dbName);

                // no object
                // this is bad. if we have error in db, and object is not created
                // or user try to modify object of another domain
                if (newClass == null) {
                    logger.warn("History error. No object with id {} domain {} committer {}", t.getId(), dbName, committerId);
                    return;
                }

                String dbForJavers = dbName;

                if (!CollectionUtils.isEmpty(classFinders)) {
                    for (BaseClassFinder x : classFinders) {
                        String db = x.forceChangeDatabaseForJavers(newClass, dbName);
                        if (db != null) {
                            dbForJavers = db;
                            break;
                        }
                    }
                }

                Javers javers = getJavers(dbForJavers);
                javers.commit(committerId, newClass);
            } catch (Exception e) {
                logger.error("Javers history in domain {}", dbName, e);
            }

        });
    }

    public String getChangesStringedLog(List<Change> changes, CurrentUserContextProvider currentUser) {
        return getJaversForCurrentUser(currentUser).processChangeList(changes, new SimpleTextChangeLog());
    }

    public List<Change> getChangesByObject(Class<? extends BaseClass> t, String id, CurrentUserContextProvider currentUser) {
        return getJaversForCurrentUser(currentUser).findChanges(QueryBuilder.byInstanceId(id, t).build());
    }

    /**
     * @param classObject  biqa object class
     * @param id           biqa object id
     * @param databaseName mongodb database = domain
     * @return
     */
    public List<Change> getChangesByObject(Class<? extends BaseClass> classObject, String id, String databaseName) {
        return getJavers(databaseName).findChanges(QueryBuilder.byInstanceId(id, classObject).build());
    }

    /**
     * Javers used internally {@link org.javers.common.collections.Optional} which is not serialized
     *
     * @param changes
     * @return
     */
    public static List<ChangeObjectDTO> transformJaversChangesToDTO(List<Change> changes) {
        List<ChangeObjectDTO> changeObjects = new ArrayList<>();

        if (CollectionUtils.isEmpty(changes)) return changeObjects;

        for (Change change : changes) {
            ChangeObjectDTO object = new ChangeObjectDTO();

            if (change.getCommitMetadata().isPresent()){
                LocalDateTime commitDate = change.getCommitMetadata().get().getCommitDate();
                Date date = Date.from(commitDate.atZone(ZoneId.systemDefault()).toInstant());
                object.setDate(date);
            }

            if (change.getCommitMetadata().isPresent()){
                object.setUserId(change.getCommitMetadata().get().getAuthor());
            }

            object.setChange(change);
            object.setAction(change.getClass().getSimpleName());

            changeObjects.add(object);
        }

        changeObjects = changeObjects.stream().sorted().collect(Collectors.toList());
        Collections.reverse(changeObjects);

        return changeObjects;
    }

    @Autowired(required = false)
    public void setClassFinders(List<BaseClassFinder> classFinders) {
        this.classFinders = classFinders;
    }
}
