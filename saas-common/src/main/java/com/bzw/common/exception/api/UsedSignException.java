package com.bzw.common.exception.api;


import com.bzw.common.exception.ApplicationException;
import com.bzw.common.exception.ApplicationErrorCode;

/**
 * Created by yanbin on 2017/7/1.
 */
public class UsedSignException extends ApplicationException {
    public UsedSignException() {
        super(ApplicationErrorCode.UsedSignature.getCode(),
                ApplicationErrorCode.UsedSignature.getReasoning());
    }
}
