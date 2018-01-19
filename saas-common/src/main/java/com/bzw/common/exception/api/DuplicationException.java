package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;

/**
 *
 * @author yanbin
 * @date 2017/7/1
 */
public class DuplicationException extends ApplicationException {

    public DuplicationException() {
        super(ApplicationErrorCode.DuplicateSubmit.getCode(), ApplicationErrorCode.DuplicateSubmit.getReasoning());
    }

    public DuplicationException(Exception e) {
        super(ApplicationErrorCode.DuplicateSubmit.getCode(), e.getMessage());
    }
}
