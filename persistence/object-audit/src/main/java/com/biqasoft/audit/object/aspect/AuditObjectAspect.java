/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object.aspect;

import com.biqasoft.audit.object.ObjectsAuditHistoryService;
import com.biqasoft.entity.core.BaseClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Aspects
 */
@Aspect
@Component
@Configuration
public class AuditObjectAspect {

    @Autowired
    private ObjectsAuditHistoryService objectsAuditHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(AuditObjectAspect.class);

    /**
     * Process object edit history
     *
     * @param joinPoint
     */
    @After(" execution(public * *(..)) && @annotation(com.biqasoft.entity.annotations.BiqaAuditObject) ")
    public void biqaAuditObject(JoinPoint joinPoint) {

        // TODO: broken for Mono return type

        Object[] params = joinPoint.getArgs();
        for (Object o : params) {
            if (o instanceof BaseClass) {
                BaseClass baseClass = (BaseClass) o;
                objectsAuditHistoryService.auditChangesForCurrentUser(baseClass);
            }
        }

    }

}