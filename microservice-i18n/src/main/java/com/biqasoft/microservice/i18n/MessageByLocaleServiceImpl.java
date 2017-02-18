/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 6/26/2016
 *         All Rights Reserved
 */
@Component
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

    private final MessageSource messageSource;

    @Autowired
    public MessageByLocaleServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(id, null, locale);
    }

    @Override
    public String getMessageEnglish(String id) {
        return messageSource.getMessage(id, null, Locale.US);
    }

    @Override
    public String getMessageByLocale(String id, String locale) {
        Locale localeObj = new Locale(locale);
        return messageSource.getMessage(id, null, localeObj);
    }

    @Override
    public String getMessageByLocale(String id, Object[] args, String locale) {
        Locale localeObj = new Locale(locale);
        return messageSource.getMessage(id, args, localeObj);    }

    @Override
    public String getMessageEnglish(String id, Object[] args) {
        return messageSource.getMessage(id, args, Locale.US);
    }

}