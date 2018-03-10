package com.biqasoft.microservice.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by ya on 3/28/2017.
 */
@Component
public class SystemExitOnSpringError implements ApplicationListener<ContextClosedEvent> {

    final ApplicationContext applicationContext;

    @Autowired
    public SystemExitOnSpringError(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        int returnCode = 1;
        SpringApplication.exit(applicationContext, () -> returnCode);
        System.exit(returnCode);
    }
}
