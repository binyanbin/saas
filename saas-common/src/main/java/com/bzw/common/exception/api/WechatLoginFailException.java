package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;

public class WechatLoginFailException extends ApplicationException {
    public WechatLoginFailException () {
        super(ApplicationErrorCode.OpenIdLoginFail.getCode(),
                ApplicationErrorCode.OpenIdLoginFail.getReasoning());
    }
}
