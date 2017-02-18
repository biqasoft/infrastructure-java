///*
// * Copyright 2016 the original author or authors.
// */
//
//package com.biqasoft.common.hacks;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
//import org.springframework.stereotype.Component;
//
//import java.util.Locale;
//
///**
// * @author Nikita Bakaev, ya@nbakaev.ru
// *         Date: 8/9/2016
// *         All Rights Reserved
// */
//@Component
//public class LocaleConfig implements BeanDefinitionRegistryPostProcessor {
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
//
//        Locale.setDefault(new Locale("en", "us"));
//        System.setProperty("user.language", "en");
//        System.setProperty("user.country", "USA");
//
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//
//    }
//}
