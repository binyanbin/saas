package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;

/**
 * Created by yanbin on 2017/7/1.
 */
public class InvalidAccessTokenException extends ApplicationException {

    public InvalidAccessTokenException() {
        super(ApplicationErrorCode.InvalidAccessToken.getCode(), ApplicationErrorCode.InvalidAccessToken.getReasoning());
    }
}
