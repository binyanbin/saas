package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;

public class EventHandleTimeoutException extends ApplicationException {

    public EventHandleTimeoutException() {
        super(ApplicationErrorCode.EventHandlerTimeout.getCode(), ApplicationErrorCode.EventHandlerTimeout.getReasoning());
    }
}
