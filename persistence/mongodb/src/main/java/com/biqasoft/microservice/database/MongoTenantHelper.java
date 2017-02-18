/*
 * Copyright 2016 the original author or authors.
 */

package com.biqasoft.microservice.database;

import com.biqasoft.entity.core.useraccount.UserAccount;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 7/28/2016
 *         All Rights Reserved
 */
@Service
@ConditionalOnBean(MongoClient.class)
public class MongoTenantHelper {

    // this is connection to user databases with admin role and credentials
    // only for internal operations
    private ConcurrentHashMap<String, MongoTemplate> allDomainsMap = new ConcurrentHashMap<>(30);
    private final MongoClient mongoClient;

    @Autowired
    public MongoTenantHelper(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }


    /**
     * Get mongoTemplate (database connection) for some domain by domain name
     * Used internally;
     * This is alternative to
     * <code>
     *
     * @param domain
     * @return
     * @Autowired
     * @Qualifier("domainBasedTemplate") private MongoTemplate ops;
     * </code>
     * And used when we can not use expression above
     */
    public MongoTemplate domainDataBaseUnsafeGet(String domain) {
        // if we have connection to domain user db
        // just return it
        MongoTemplate mongoTemplate = allDomainsMap.get(domain);
        if (mongoTemplate != null) {
            return mongoTemplate;
        }

        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, domain);

        // if we have not - create connection
        mongoTemplate = MongoHelpers.MongoConverter(mongoDbFactory);
        allDomainsMap.put(domain, mongoTemplate);

        return mongoTemplate;
    }


    public Map<String, MongoTemplate> getAllDomainsMap() {
        return allDomainsMap;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public static UserAccount createMockedUser(String domain){
        UserAccount userAccount = new UserAccount();
        userAccount.setId("SYSTEM_USER");
        return userAccount;
    }

}
