/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object.aspect;

import com.biqasoft.audit.object.ObjectsAuditHistoryService;
import com.biqasoft.auth.CurrentUserContextProvider;
import com.biqasoft.entity.constants.SystemRoles;
import com.biqasoft.entity.core.BaseClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Aspects
 */
@Aspect
@Component
@Configuration
public class AuditObjectSecurityModifyAspect extends BaseAspect {

    @Autowired
    private ObjectsAuditHistoryService objectsAuditHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(AuditObjectSecurityModifyAspect.class);

    /**
     * ALPHA.
     * not correctly implemented yet
     * Check that user have permission to modify object
     *
     * @param joinPoint
     */
    @After(" execution(public * *(..)) && @annotation(com.biqasoft.entity.annotations.BiqaCheckSecuredModifyObject) ")
    public void biqaCheckSecuredModifyObject(JoinPoint joinPoint) {

        Object[] params = joinPoint.getArgs();
        for (Object o : params) {
            if (o instanceof BaseClass) {

                Optional<CurrentUserContextProvider> first = getAuthContextFromParams(params);

                if (!first.isPresent()) {
                    throw new IllegalStateException("biqaCheckSecuredModifyObject with not existed CurrentUserContextProvider param");
                } else {

                    BaseClass baseClass = (BaseClass) o;
                    BaseClass classFromDataBase = null;

                    CurrentUserContextProvider currentUser = first.get();

                    try {
                        classFromDataBase = objectsAuditHistoryService.getBiqaClassById(baseClass, currentUser.getDomain().getDomain());
                        if (classFromDataBase == null) {
                            continue;
                        }
                    } catch (Exception e) {
                        logger.warn("Error biqaCheckSecuredModifyObject with id {} class {}", baseClass.getId(), e.getClass().toString(), e);
                    }

                    // if object is marked as secured and user want to delete it
                    // and user is not admin - deny this operation
                    if (classFromDataBase != null && classFromDataBase.isSecured() &&
                            !currentUser.hasRole(SystemRoles.ROLE_ADMIN)) {
                        throw new AccessDeniedException("Only user with admin rights can modify or delete secured objects");
                    }
                }

            }
        }

    }

}