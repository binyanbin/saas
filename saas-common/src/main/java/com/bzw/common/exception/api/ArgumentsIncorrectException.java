package com.bzw.common.exception.api;

import com.bzw.common.exception.ApplicationException;
import com.bzw.common.exception.ApplicationErrorCode;

/**
 *
 * @author yanbin
 * @date 2017/7/1
 */
public class ArgumentsIncorrectException extends ApplicationException {

    public ArgumentsIncorrectException() {
        super(ApplicationErrorCode.ArgumentsIncorrect.getCode(), ApplicationErrorCode.ArgumentsIncorrect.getReasoning());
    }

    public ArgumentsIncorrectException(String reasoning) {
        super(ApplicationErrorCode.ArgumentsIncorrect.getCode(), reasoning);
    }
}
