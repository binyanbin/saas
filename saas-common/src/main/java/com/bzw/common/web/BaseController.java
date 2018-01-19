package com.bzw.common.web;

/**
 * @author yanbin
 */
public abstract class BaseController {
    /**
     * wrapper JSON view and set message
     * @param object
     * @return
     */
    public Object wrapperJsonView(Object object, String message) {
        return new JsonWrapper(object, message);
    }

    /**
     *  wrapper JSON view
     * @param object
     * @return
     */
    public Object wrapperJsonView(Object object) {
        return new JsonWrapper(object);
    }

    /**
     *   wrapper exception JSON view
     * @param throwable
     * @return
     */
    public Object wrapperExceptionJsonView(Throwable throwable) {
        return new JsonExceptionWrapper(throwable);
    }

}
