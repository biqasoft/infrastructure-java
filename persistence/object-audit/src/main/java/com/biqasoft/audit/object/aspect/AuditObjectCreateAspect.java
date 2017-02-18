/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object.aspect;

import com.biqasoft.audit.object.BiqaObjectValidatorRestService;
import com.biqasoft.entity.annotations.BiqaAddObject;
import com.biqasoft.entity.core.BaseClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
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
public class AuditObjectCreateAspect {

    @Autowired
    private BiqaObjectValidatorRestService biqaObjectValidatorRestService;

    private static final Logger logger = LoggerFactory.getLogger(AuditObjectCreateAspect.class);

    /**
     * Add {@link BiqaAddObject} to public method in API and every {@link BaseClass} param in method
     * will be modified and added {@link com.biqasoft.entity.core.CreatedInfo}
     *
     * @param joinPoint
     */
    @Before(" execution(public * *(..)) && @annotation(com.biqasoft.entity.annotations.BiqaAddObject) ")
    public void beforeSaveAspect(JoinPoint joinPoint) {

        Object[] params = joinPoint.getArgs();
        for (Object o : params) {
            if (o instanceof BaseClass) {

                MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
                BiqaAddObject annotation = methodSignature.getMethod().getAnnotation(BiqaAddObject.class);
                if (annotation == null) {
                    logger.error("Hm... why we are here");
                } else {
                    biqaObjectValidatorRestService.checkAndSetDefaultBiqa((BaseClass) o, annotation.forceAddCustomField());
                }

            }
        }

    }

}