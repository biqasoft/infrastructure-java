/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.common.exceptions;

/**
 * This is exception to just quick response user in API
 * that request is invalid.
 * Do not prints exception to stack trace
 */
@SuppressWarnings("serial")
public class InvalidRequestLocalizedException extends RuntimeException {

    public InvalidRequestLocalizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    // do not add stacktrace
    public InvalidRequestLocalizedException(String message) {
        super(message, null, false, false);
    }

}