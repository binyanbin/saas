package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;

/**
 * @author yanbin
 */
public class WrongSmsCodeException extends ApplicationException {
    public WrongSmsCodeException(){
        super(ApplicationErrorCode.WrongSmsCode.getCode(),
                ApplicationErrorCode.WrongSmsCode.getReasoning());
    }
}
