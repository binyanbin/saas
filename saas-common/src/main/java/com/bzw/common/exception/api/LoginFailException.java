package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;

public class LoginFailException extends ApplicationException {
    public LoginFailException() {
        super(ApplicationErrorCode.LoginFail.getCode(),
                ApplicationErrorCode.LoginFail.getReasoning());
    }
}
