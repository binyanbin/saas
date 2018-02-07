package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;

/**
 * @author yanbin
 */
public class PhoneNotExisitsException extends ApplicationException {
    public PhoneNotExisitsException() {
        super(ApplicationErrorCode.PhoneNotExists.getCode(),
                ApplicationErrorCode.PhoneNotExists.getReasoning());
    }
}
