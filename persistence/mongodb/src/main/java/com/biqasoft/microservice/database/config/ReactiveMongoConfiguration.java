/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.database.config;

import com.biqasoft.microservice.database.MongoHelpers;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class ReactiveMongoConfiguration {

    private String databaseServiceName;
    private String databaseName;
    private String userName;
    private String database;
    private String password;
    private static final Logger logger = LoggerFactory.getLogger(ReactiveMongoConfiguration.class);
    private final DiscoveryClient discoveryClient;

    /**
     * @param databaseServiceName service name in consul. all common in consul with this name will be interpreted as mongodb cluster nodes
     * @param mainDatabase        name of main authDatabase in mongodb
     * @param username            username of mongodb
     * @param authDatabase        authDatabase to which user of mongo client will be authenticated. for example admin authDatabase
     * @param password            password of mongodb
     */
    @Autowired
    public ReactiveMongoConfiguration(@Value("${db.discovery.service.name}") String databaseServiceName,
                                      @Value("${db.database.main.name}") String mainDatabase,
                                      @Value("${db.credentials.username}") String username,
                                      @Value("${db.credentials.db}") String authDatabase,
                                      @Value("${db.credentials.password}") String password,
                                      DiscoveryClient discoveryClient) {
        this.databaseServiceName = databaseServiceName;
        this.databaseName = mainDatabase;
        this.userName = username;
        this.database = authDatabase;
        this.password = password;
        this.discoveryClient = discoveryClient;
    }

    @Bean
    ReactiveMongoOperations mainReactiveDataBaseTemplate() throws Exception {
        SimpleReactiveMongoDatabaseFactory mongoDbFactory = new SimpleReactiveMongoDatabaseFactory(reactiveMongoClient(), databaseName);

        ReactiveMongoOperations mongoOperations = MongoHelpers.MongoReactiveConverter(mongoDbFactory);
        return mongoOperations;
    }

    /**
     * Get mongo addressed and ports in consul service discovery
     *
     * @return
     */
    private List<ServiceInstance> getMongoServices() {
        List<ServiceInstance> list = discoveryClient.getInstances(databaseServiceName);

        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("Can not find mongodb server. Response from consul in null or empty");
        }
        return list;
    }

    @Bean
    MongoClient reactiveMongoClient() throws Exception {
        MongoClient mongoClient;
        List<ServerAddress> serverAddresses = new ArrayList<>();

        try {
            List<ServiceInstance> serviceInstance = getMongoServices();

            for (ServiceInstance instance : serviceInstance) {
                serverAddresses.add(new ServerAddress(instance.getHost(), instance.getPort()));
            }

        } catch (Exception e) {
            logger.error("Can not get mongo servers", e);
        }

        MongoCredential mongoCredentials = MongoCredential.createScramSha1Credential(
                userName,
                database,
                password.toCharArray());

        List<MongoCredential> mongoCredentialsList = new LinkedList<>();
        mongoCredentialsList.add(mongoCredentials);

        MongoClientSettings.Builder mongoClientOptionsBuilder = MongoClientSettings.builder();
        mongoClientOptionsBuilder.writeConcern(WriteConcern.MAJORITY);
        mongoClientOptionsBuilder.credential(mongoCredentials);
        mongoClientOptionsBuilder.clusterSettings(ClusterSettings.builder().hosts(serverAddresses).build());

        mongoClient = MongoClients.create(mongoClientOptionsBuilder.build());
        return mongoClient;
    }

}