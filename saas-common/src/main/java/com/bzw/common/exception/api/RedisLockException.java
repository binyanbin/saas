package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationException;
import com.bzw.common.exception.ApplicationErrorCode;

/**
 * Created by yanbin on 2017/7/10.
 */
public class RedisLockException extends ApplicationException{

    public RedisLockException() {
        super(ApplicationErrorCode.RedisLockException.getCode(), ApplicationErrorCode.RedisLockException.getReasoning());
    }
}
