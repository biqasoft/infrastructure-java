/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.database;

import com.biqasoft.microservice.database.converters.BigDecimalToDecimal128Converter;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import com.biqasoft.microservice.database.converters.BigDecimalToBigDecimalConverter;

import java.util.Arrays;

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
}
