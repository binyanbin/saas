package com.bzw.common.web;

/**
 * @author yanbin
 * @date 2017/7/5
 */
public class JsonExceptionWrapper {
    private String code;
    private String msg;
    private String stackTrace;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
