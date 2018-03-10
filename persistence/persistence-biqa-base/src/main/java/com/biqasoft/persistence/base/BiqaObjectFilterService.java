/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.persistence.base;

import com.biqasoft.common.exceptions.ThrowExceptionHelper;
import com.biqasoft.entity.annotations.BiqaDontOverrideField;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.core.BaseClassFilter;
import com.biqasoft.entity.core.RegexBuilderNode;
import com.biqasoft.entity.format.BiqaPaginationResultList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create mongodb db criteria and Query for {@link BaseClass} from {@link BaseClassFilter}
 */
@Service
public class BiqaObjectFilterService {

    private static final Logger logger = LoggerFactory.getLogger(BiqaObjectFilterService.class);
    private final DateService dateService;
    private final FindAndModifyOptions findAndModifyOptions;

    @Autowired
    public BiqaObjectFilterService(DateService dateService) {
        this.dateService = dateService;
        this.findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.returnNew(true);
    }

    /**
     * @param baseClass new updated object that will be updated
     * @param ops       MongoTemplate - this is user domain connection database
     * @param <T>
     * @return
     */
    public <T extends BaseClass> T safeUpdate(T baseClass, MongoOperations ops) {
        return safeUpdate(baseClass, ops, true);
    }

    public <T extends BaseClass> T safeUpdate(T baseClass, MongoOperations ops, boolean useOptimisticLock) {
        List<Field> fieldsToUpdate = getAllFields(new LinkedList<>(), baseClass.getClass());
        fieldsToUpdate.forEach(x -> x.setAccessible(true));
        return safeUpdateWithOptimisticLockSupport(baseClass, ops, useOptimisticLock, fieldsToUpdate);
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseClass> T safeUpdateWithOptimisticLockSupport(T baseClass, MongoOperations ops, boolean useOptimisticLock, List<Field> fieldsToUpdate) {
        Update update = new Update();

        for (Field field : fieldsToUpdate) {
            try {
                if (field.get(baseClass) != null) {
                    update.set(field.getName(), field.get(baseClass));
                }

            } catch (IllegalAccessException e) {
                logger.error("Error", e);
            }
        }

        Criteria criteria = new Criteria();
        criteria.and("id").is(baseClass.getId());

        // optimistic lock
        if (!StringUtils.isEmpty(baseClass.getVersion()) & useOptimisticLock) {
            criteria.and("version").is(baseClass.getVersion());
        }

        Query query = new Query(criteria);
        update.set("version", baseClass.getVersion() + 1);

        T modifiedEntity = (T) ops.findAndModify(query, update, findAndModifyOptions, baseClass.getClass());
        if (modifiedEntity == null) {
            ThrowExceptionHelper.throwExceptionInvalidRequestLocalized("entity.update.concurrent.lock.optimistic.error");
            return null;
        } else {
            return modifiedEntity;
        }
    }

    /**
     * update BaseClass class with all fields, only for internal use, where want save entire object with optimistic lock
     * @param baseClass
     * @param ops
     * @param useOptimisticLock
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseClass> T unsafeUpdate(T baseClass, MongoOperations ops, boolean useOptimisticLock) {
        List<Field> fieldsToUpdate = getAllFieldsWithBiqaDontOverrideField(new LinkedList<>(), baseClass.getClass());
        fieldsToUpdate.forEach(x -> x.setAccessible(true));
        return safeUpdateWithOptimisticLockSupport((T) baseClass, ops, useOptimisticLock, fieldsToUpdate);
    }

    /**
     * Get mongodb criteria from filter
     * BE CAREFUL - THIS METHOD NO LONGER ADD `DOMAIN` - you should do it manually
     *
     * @param filter
     * @return
     */
    public Criteria getCriteriaFromAbstractBuilder(BaseClassFilter filter) {
        Criteria criteria = new Criteria();

        // regex filter
        if (filter.isUseRegex() && !CollectionUtils.isEmpty(filter.getRegexBuilderNodes())) {
            for (RegexBuilderNode node : filter.getRegexBuilderNodes()) {

                if (node.getFieldName() != null && node.getPattern() != null && node.getValue() == null) {
                    criteria.and(node.getFieldName()).regex(node.getPattern());

                } else if (node.getFieldName() != null && node.getValue() != null && node.getPattern() == null) {
                    criteria.and(node.getFieldName()).is(node.getValue());
                }
            }
        }

        // don't show archived objects by default
        if (!filter.isShowArchived()) criteria.and("archived").is(false);

        // object alias
        if (!CollectionUtils.isEmpty(filter.getAliases())) {
            criteria.and("alias").in(filter.getAliases());
        }

        // only created by some user id
        if (filter.isUseCreatedByUserId())
            criteria.and("createdInfo.createdById").is(filter.getCreatedByUserId());

        if (filter.isUseObjectIds())
            criteria.and("id").in(filter.getObjectIds());

        // created date
        if (filter.isUseRelativeCreatedDateFrom() && !filter.isUseRelativeCreatedDateTo()) {
            criteria.and("createdInfo.createdDate").gte(dateService.parseDateExpression(filter.getRelativeCreatedDateFrom()));
        }

        if (!filter.isUseRelativeCreatedDateFrom() && filter.isUseRelativeCreatedDateTo()) {
            criteria.and("createdInfo.createdDate").lte(dateService.parseDateExpression(filter.getRelativeCreatedDateTo()));
        }

        if (filter.isUseRelativeCreatedDateFrom() && filter.isUseRelativeCreatedDateTo()) {
            Date dateFrom = dateService.parseDateExpression(filter.getRelativeCreatedDateFrom());
            Date dateTo = dateService.parseDateExpression(filter.getRelativeCreatedDateTo());
            criteria.and("createdInfo.createdDate").lte(dateTo).gte(dateFrom);
        }

        return criteria;
    }

    /**
     * Get mongodb query from filter and criteria(for example from {@link #getCriteriaFromAbstractBuilder(BaseClassFilter)})
     *
     * @param filter
     * @param criteria
     * @return
     */
    public Query getQueryFromFilter(BaseClassFilter filter, Criteria criteria) {
        Query query;

        // mongodb full text search
        if (!filter.isUseFullTextSearch()) {
            query = new Query(criteria);
        } else {
            TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(filter.getFullTextSearchRequest());
            query = TextQuery.queryText(textCriteria).sortByScore().addCriteria(criteria);
        }

        // reverse sort
        if (filter.isSortDESCbyCreatedDate() && !filter.isUseCustomSort()) {
            query.with(new Sort(Sort.Direction.DESC, "createdInfo.createdDate"));
        }

        // sort by custom field
        if (filter.isUseCustomSort()) {
            if (filter.isReverseSortDirectionByCustomField()) {
                query.with(new Sort(Sort.Direction.DESC, filter.getCustomSortField()));
            } else {
                query.with(new Sort(Sort.Direction.ASC, filter.getCustomSortField()));
            }
        }

        // get only some part of resulted entities
        if (filter.isUsePagination()) {
            query.skip(filter.getRecordFrom());
            query.limit(filter.getRecordTo() - filter.getRecordFrom());
        }

        // return from data base only some fields
        if (filter.isUseFieldsPartly()) {
            filter.getPartlyFields().forEach(x -> query.fields().include(x));
        }

        // exclude some fields from result
        if (filter.isUseExcludeFieldsPartly()) {
            filter.getPartlyExcludeFields().forEach(x -> query.fields().exclude(x));
        }

        return query;
    }

    /**
     * With full text support
     *
     * @param filter
     * @param criteria
     * @param objectType
     * @param ops
     * @return
     */
    public long countResultFromCriteria(BaseClassFilter filter, Criteria criteria, Class objectType, MongoOperations ops) {
        return countResultFromCriteria(filter, criteria, objectType, ops, null);
    }

    /**
     * With full text support
     *
     * @param filter
     * @param criteria
     * @param objectType
     * @param collectionName db collection name. Null if search in default collection
     * @param ops
     * @return
     */
    public long countResultFromCriteria(BaseClassFilter filter, Criteria criteria, Class objectType, MongoOperations ops, String collectionName) {
        long resultEntity;
        Query query;

        // count of entities with full text search
        if (!filter.isUseFullTextSearch()) {
            query = new Query(criteria);
        } else {
            TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(filter.getFullTextSearchRequest());
            query = TextQuery.queryText(textCriteria).sortByScore().addCriteria(criteria);
        }

        if (collectionName == null) {
            resultEntity = ops.count(query, objectType);
        } else {
            resultEntity = ops.count(query, objectType, collectionName);
        }

        return resultEntity;
    }

    /**
     * Use default mongodb collection by class
     *
     * @param filter
     * @param criteria
     * @param query
     * @param objectType
     * @param ops
     * @param <T>
     * @return
     */
    public <T extends BaseClass> BiqaPaginationResultList<T> getPaginationResultList(BaseClassFilter filter, Criteria criteria, Query query, Class<T> objectType,
                                                                                     MongoOperations ops) {
        return getPaginationResultList(filter, criteria, query, objectType, ops, null);
    }

    public <T extends BaseClass> long countAllEntityNumber(BaseClassFilter filter, Criteria criteria, Query query, Class<T> objectType,
                                                           MongoOperations ops, String collectionName) {

        if (collectionName == null) {
            return countResultFromCriteria(filter, criteria, objectType, ops);
        } else {
            return countResultFromCriteria(filter, criteria, objectType, ops, collectionName);
        }
    }

    /**
     * @param filter
     * @param criteria
     * @param query
     * @param objectType
     * @param ops
     * @param collectionName custom collection to search
     * @param <T>
     * @return
     */
    public <T extends BaseClass> BiqaPaginationResultList<T> getPaginationResultList(BaseClassFilter filter, Criteria criteria, Query query, Class<T> objectType,
                                                                                     MongoOperations ops, String collectionName) {
        BiqaPaginationResultList<T> biqaPaginationResultList = new BiqaPaginationResultList<>();

        if (filter.isOnlyCount()) {
            biqaPaginationResultList.setEntityNumber(countAllEntityNumber(filter, criteria, query, objectType, ops, collectionName));
            return biqaPaginationResultList;
        }

        // if we don't use pagination, we can just count resulted objects and don't do extra query
        if (filter.isUsePagination()) {
            if (collectionName == null) {
                biqaPaginationResultList.setResultedObjects(ops.find(query, objectType));
            } else {
                biqaPaginationResultList.setResultedObjects(ops.find(query, objectType, collectionName));
            }
            biqaPaginationResultList.setEntityNumber(countAllEntityNumber(filter, criteria, query, objectType, ops, collectionName));
        } else {
            // we have full result - use java list size instead of one more query to db
            if (collectionName == null) {
                biqaPaginationResultList.setResultedObjects(ops.find(query, objectType));
            } else {
                biqaPaginationResultList.setResultedObjects(ops.find(query, objectType, collectionName));
            }
            biqaPaginationResultList.setEntityNumber(biqaPaginationResultList.getResultedObjects().size());
        }

        return biqaPaginationResultList;
    }

    /**
     * Search with default {@link Query} and {@link Criteria}
     *
     * @param filter
     * @param objectType
     * @param ops
     * @param collectionName
     * @param <T>
     * @return
     */
    public <T extends BaseClass> BiqaPaginationResultList getPaginationResultList(BaseClassFilter filter, Class<T> objectType, MongoOperations ops, String collectionName) {
        Criteria criteria = getCriteriaFromAbstractBuilder(filter);
        Query query = getQueryFromFilter(filter, criteria);
        return getPaginationResultList(filter, criteria, query, objectType, ops, collectionName);
    }

    /**
     * recursive get fields which should be updated on HTTP PUT
     * including fields from super class
     *
     * @param fields clean List
     * @param type   class
     * @return result
     */
    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {

        // if type has not annotation
        if (!type.isAnnotationPresent(BiqaDontOverrideField.class)) {
            // if some fields have not annotation
            fields.addAll(Arrays.stream(type.getDeclaredFields()).filter(field -> !field.isAnnotationPresent(BiqaDontOverrideField.class)).collect(Collectors.toList()));
        }

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    /**
     * recursive get fields which should be updated on HTTP PUT
     * including fields from super class
     *
     * @param fields clean List
     * @param type   class
     * @return result
     */
    private static List<Field> getAllFieldsWithBiqaDontOverrideField(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.stream(type.getDeclaredFields()).collect(Collectors.toList()));

        if (type.getSuperclass() != null) {
            fields = getAllFieldsWithBiqaDontOverrideField(fields, type.getSuperclass());
        }
        return fields;
    }

}
