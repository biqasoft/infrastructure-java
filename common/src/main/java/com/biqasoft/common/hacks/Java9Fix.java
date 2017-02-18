/*
 * Copyright 2016 the original author or authors.
 */

package com.biqasoft.common.hacks;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 8/9/2016
 *         All Rights Reserved
 */
@Component
public class Java9Fix implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        try {
            if (System.getProperty("java.version").equals("9-ea") || System.getProperty("java.version").startsWith("9")) {
                System.setProperty("java.version", "1.9");
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
