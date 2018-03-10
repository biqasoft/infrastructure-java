/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.database;

import com.biqasoft.microservice.database.converters.BigDecimalToBigDecimalConverter;
import com.biqasoft.microservice.database.converters.BigDecimalToDecimal128Converter;
import com.mongodb.DBRef;
import org.bson.Document;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nikita Bakaev, ya@nbakaev.ru on 5/9/2016.
 * All Rights Reserved
 */
public class MongoHelpers {
    public static MongoTemplate MongoConverter(MongoDbFactory mongoDbFactory) {
        MappingContext mappingContext = new MongoMappingContext();

        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), mappingContext);
//        converter.setTypeMapper(mapper);
        converter.setCustomConversions(new CustomConversions(
                Arrays.asList(
                        new BigDecimalToDecimal128Converter(),
                        new BigDecimalToBigDecimalConverter()
                )
        ));
        converter.afterPropertiesSet();
        return new MongoTemplate (mongoDbFactory, converter);
    }

    public static ReactiveMongoTemplate MongoReactiveConverter(ReactiveMongoDatabaseFactory mongoDbFactory) {
        MappingContext mappingContext = new MongoMappingContext();

        MappingMongoConverter converter = new MappingMongoConverter( new NoopRef(), mappingContext);
//        converter.setTypeMapper(mapper);
        converter.setCustomConversions(new CustomConversions(
                Arrays.asList(
                        new BigDecimalToDecimal128Converter(),
                        new BigDecimalToBigDecimalConverter()
                )
        ));
        converter.afterPropertiesSet();
        return new ReactiveMongoTemplate (mongoDbFactory, converter);
    }

    // TODO: https://jira.spring.io/browse/DATAMONGO-1584
    public static class NoopRef implements DbRefResolver {

        @Override
        public Object resolveDbRef(MongoPersistentProperty property, DBRef dbref, DbRefResolverCallback callback, DbRefProxyHandler proxyHandler) {
            return null;
        }

        @Override
        public DBRef createDbRef(org.springframework.data.mongodb.core.mapping.DBRef annotation, MongoPersistentEntity<?> entity, Object id) {
            return null;
        }

        @Override
        public Document fetch(DBRef dbRef) {
            return null;
        }

        @Override
        public List<Document> bulkFetch(List<DBRef> dbRefs) {
            return null;
        }
    }

}
