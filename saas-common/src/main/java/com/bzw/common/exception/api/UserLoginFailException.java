package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;

/**
 * @author yanbin
 */
public class UserLoginFailException extends ApplicationException {
    public UserLoginFailException() {
        super(ApplicationErrorCode.UserLoginFail.getCode(),
                ApplicationErrorCode.UserLoginFail.getReasoning());
    }
}
