/*
 * Copyright 2016 the original author or authors.
 */

package com.biqasoft.microservice.configs;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.biqasoft.microservice.configs.LoggerConfigHelper.GenerateRequestIdStrategy.MONGODB_ID;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 7/20/2016
 *         All Rights Reserved
 *
 * Benchmark                            Mode  Cnt         Score        Error  Units
 * MyBenchmark.testLoggerConfigHelper  thrpt   40  18357223.538 ± 393155.350  ops/s
 * MyBenchmark.testMongoId             thrpt   40  18479378.308 ± 419078.434  ops/s
 * MyBenchmark.testuid                 thrpt   40   1244018.997 ±  20201.204  ops/s
 * <code>
 *
 *  @State(Scope.Thread)
 *  public class MyBenchmark {
 *
 *  @Benchmark
 *  public String testMongoId() {
 *      String id = new ObjectId().toString();
 *      return id;
 *  }
 *
 *  @Benchmark
 *  public String testuid() {
 *      String id = UUID.randomUUID().toString();
 *      return id;
 *  }
 *  @Benchmark
 *  public String testLoggerConfigHelper() {
 *      String id = LoggerConfigHelper.generateRequestId();
 *      return id;
 *  }
 *
 *  }
 * </code>
 *
 */
@Component
public class LoggerConfigHelper {

    private static String generateRequestIdStrategy = MONGODB_ID.name();

    public final static String REQUEST_ID_LOGGER = "RequestId";

    public static String generateRequestId(){
        if (generateRequestIdStrategy.equals(GenerateRequestIdStrategy.MONGODB_ID.name())){
            return new ObjectId().toString();
        }else if (generateRequestIdStrategy.equals(GenerateRequestIdStrategy.UUID.name())) {
            return UUID.randomUUID().toString();
        }

        return new ObjectId().toString();
    }

    public static String getGenerateRequestIdStrategy() {
        return generateRequestIdStrategy;
    }

    @Autowired
    public void setGenerateRequestIdStrategy(@Value("${biqa.generate_request_id_strategy:MONGODB_ID}") String generateRequestIdStrategy) {
        LoggerConfigHelper.generateRequestIdStrategy = generateRequestIdStrategy;
    }

   enum GenerateRequestIdStrategy{
        MONGODB_ID,
        UUID
    }

}
