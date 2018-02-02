package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;

public class NotFoundImageException extends ApplicationException {

    public NotFoundImageException() {
        super(ApplicationErrorCode.NotFoundImage.getCode(), ApplicationErrorCode.NotFoundImage.getReasoning());
    }

    public NotFoundImageException(Exception e) {
        super(ApplicationErrorCode.NotFoundImage.getCode(), e.getMessage());
    }
}
