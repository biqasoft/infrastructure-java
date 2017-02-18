/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.common.exceptions;

import com.biqasoft.common.watchablevalue.WatchableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 6/9/2016
 *         All Rights Reserved
 */
@Component
public class ThrowExceptionHelper{

    private final static Logger logger = LoggerFactory.getLogger(ThrowExceptionHelper.class);
    private static Settings printExceptionStrategy = Settings.DO_NOT_PRINT;

    public static void throwExceptionInvalidRequest(String message) throws InvalidRequestException {

        if (printExceptionStrategy.equals(Settings.DO_NOT_PRINT)) {
            throw new InvalidRequestException(message, null, false, false);
        } else if (printExceptionStrategy.equals(Settings.PRINT_STACKTRACE)) {
            InvalidRequestException exception = new InvalidRequestException(message);
            logger.info("Throw exception", exception);
            throw exception;
        }

        // if not set Settings
        throw new InvalidRequestException(message, null, false, false);
    }

    /**
     * Catched by {@link com.biqasoft.gateway.configs.exceptionhandler.MyExceptionHandler}
     *
     * @param messageId id of i18n property in resource folder
     */
    public static void throwExceptionInvalidRequestLocalized(String messageId) throws InvalidRequestException {
        throw new InvalidRequestLocalizedException(messageId);
    }

    @Autowired
    public void setSettings(@Value("${biqa.internal.exception.strategy:DO_NOT_PRINT}") String param) {
        switch (param) {
            case "DO_NOT_PRINT":
                printExceptionStrategy = Settings.DO_NOT_PRINT;
                break;
            case "PRINT_STACKTRACE":
                printExceptionStrategy = Settings.PRINT_STACKTRACE;
                break;
        }
    }

    @WatchableValue("biqa.internal.exception.strategy")
    public void onStrategyUpdated(String newValue) {
        setSettings(newValue);
    }
}

enum Settings {
    DO_NOT_PRINT,
    PRINT_STACKTRACE,
}