package com.bzw.common.web;

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
}
