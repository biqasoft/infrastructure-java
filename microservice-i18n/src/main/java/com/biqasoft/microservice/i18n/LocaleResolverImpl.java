/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.i18n;

import com.biqasoft.entity.core.useraccount.UserAccount;
import com.biqasoft.entity.core.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 6/26/2016
 *         All Rights Reserved
 */
@Component
public class LocaleResolverImpl extends AbstractLocaleResolver {

    // we can have not CurrentUser implementation and use locale by Accept_language header
    @Autowired(required = false)
    private CurrentUser currentUser;

    /**
     * Get locale by user specific language {@link UserAccount#getLanguage()}
     *
     * @param httpServletRequest
     * @return
     */
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {

        if (currentUser == null){
            return httpServletRequest.getLocale();
        }

        try {
            return new Locale(currentUser.getLanguage());
        }catch (Exception e){
           return httpServletRequest.getLocale();
        }

    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
