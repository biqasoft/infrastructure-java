/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.microservice.i18n;

import java.util.Locale;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 6/26/2016
 *         All Rights Reserved
 */
public interface MessageByLocaleService {

    /**
     * Get translated message for user locale
     * @param id message id
     * @return translated message
     */
    String getMessage(String id);

    /**
     * Get message in english locale
     * ignore user locale
     * @param id message id
     * @return translated message
     */
    String getMessageEnglish(String id);

    /**
     * Get message in requested locale
     * @param id message id
     * @param locale locale name
     * @return translated message
     */
    String getMessageByLocale(String id, String locale);


    /**
     * see {@link org.springframework.context.MessageSource#getMessage(String, Object[], Locale)}
     * @param id
     * @param args
     * @param locale
     * @return
     */
    String getMessageByLocale(String id, Object[] args, String locale);

    String getMessageEnglish(String id, Object[] args);

}
