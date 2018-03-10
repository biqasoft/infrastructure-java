/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 6/26/2016
 *         All Rights Reserved
 */
@Configuration
public class LocaleConfiguration {

//    @Bean
//    public LocaleResolver localeResolver() {
//       return new LocaleResolverImpl();
//    }

    /**
     * Put property files in /resources/locale to message source
     * and then read this properties depend on Locale
     * @return
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:locale/messages");
        messageSource.setCacheSeconds(3600); //refresh cache once per hour
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
