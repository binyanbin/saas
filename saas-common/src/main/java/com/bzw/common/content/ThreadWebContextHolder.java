package com.bzw.common.content;

/**
 *
 * @author yanbin
 * @date 2017/7/1
 */
public class ThreadWebContextHolder {
    private static final ThreadLocal<WebContext> THREAD_LOCAL = new ThreadLocal<>();

    public static WebContext getContext() {
        if (THREAD_LOCAL.get() != null) {
            return THREAD_LOCAL.get();
        } else {
            return null;
        }
    }

    public static void setContext(WebContext value) {
        THREAD_LOCAL.set(value);
    }

    public static void removeContext() {
        WebContext session = THREAD_LOCAL.get();
        if (null != session) {
            THREAD_LOCAL.remove();
        }
    }
}
