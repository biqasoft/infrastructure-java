/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.common.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 * Date: 6/9/2016
 * All Rights Reserved
 */
@Component
public class ThrowExceptionHelper {

    public static void throwExceptionInvalidRequest(String message) throws InvalidRequestException {
        throw new InvalidRequestException(message, null, false, false);
    }

    /**
     * Catched by {@link com.biqasoft.gateway.configs.exceptionhandler.MyExceptionHandler}
     *
     * @param messageId id of i18n property in resource folder
     */
    public static void throwExceptionInvalidRequestLocalized(String messageId) throws InvalidRequestException {
        throw new InvalidRequestLocalizedException(messageId, null, false, false);
    }
}
