package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationException;
import com.bzw.common.exception.ApplicationErrorCode;

/**
 *
 * @author yanbin
 * @date 2017/7/1
 */
public class InvalidSignException extends ApplicationException {
    public InvalidSignException() {
        super(ApplicationErrorCode.InvalidSign.getCode(),
                ApplicationErrorCode.InvalidSign.getReasoning());
    }
}
