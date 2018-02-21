package com.bzw.common.web;

import com.bzw.common.system.StatusType;

/**
 * @author yanbin
 */
public abstract class BaseController {

    public Object wrapperJsonView(Object object, String message) {
        return new JsonWrapper(object, message);
    }

    public Object wrapperJsonView(Object object) {
        return new JsonWrapper(object);
    }

    public Object wrapperInvalidArgument(String msg){
        JsonExceptionWrapper exceptionWrapper = new JsonExceptionWrapper();
        exceptionWrapper.setCode(String.valueOf(StatusType.InvalidArgument.getStatusCode()));
        exceptionWrapper.setMsg(msg);
        return exceptionWrapper;
    }

    public Object wrapperBusinessException(String msg){
        JsonExceptionWrapper exceptionWrapper = new JsonExceptionWrapper();
        exceptionWrapper.setCode(String.valueOf(StatusType.BusinessException.getStatusCode()));
        exceptionWrapper.setMsg(msg);
        return exceptionWrapper;
    }

    public Object wrapperException(Integer code,String msg,String exception){
        JsonExceptionWrapper exceptionWrapper = new JsonExceptionWrapper();
        exceptionWrapper.setCode(code.toString());
        exceptionWrapper.setMsg(msg);
        exceptionWrapper.setStackTrace(exception);
        return exceptionWrapper;
    }

}
