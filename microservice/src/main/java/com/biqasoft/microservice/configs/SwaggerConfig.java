/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.configs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Create Swagger for all microservices including internal
 */
// TODO: wait web flux support https://github.com/springfox/springfox/issues/1773
@ConditionalOnClass(name = "javax/servlet/ServletContext")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // ignore spring actuator endpoints
    private boolean checkInternal(String s) {
        if (s.startsWith("/internal") || s.startsWith("/error") || s.startsWith("/health") || s.startsWith("/metrics") ) return false;
        return true;
    }

    @Bean
    public Docket swaggerSpringMvcPlugin() {

        Docket docket = new Docket(DocumentationType.SWAGGER_2).ignoredParameterTypes().apiInfo(new ApiInfo(
                "biqa Platform API Documentation",
                "If you have any question, you can contact using email 'support@biqasoft.com'. You can use 'https://api.biqasoft.com/gateway-docs/' as a cloud API url.",
                "2016",
                "https://developers.biqasoft.com",
                "support@biqasoft.com",
                "https://developers.biqasoft.com",
                "https://developers.biqasoft.com"
        ));

        docket = docket.select().paths(x -> checkInternal(x)).build();

        return docket;
    }

}