package com.bzw.common.system;

/**
 * @author yanbin
 */
public enum StatusType {
    IsOK(200, "业务正常处理", "业务正常处理，返回200"),
    InvalidArgument(400, "参数存在错误", "参数存在错误，需要验证参数大小，范围，是否必须等等。若有问题，则返回400。"),
    BusinessException(500, "业务处理错误", "业务处理错误，抛出业务处理异常，拦截后返回500。"),
    ApplicationBug(600, "程序存在bug", "程序存在bug，抛出未检查异常，拦截后返回600。"),
    ApplicationException(700, "应用程序错误", "应用程序错误，抛出已检查异常，拦截后返回700"),
    ;
    private int statusCode;
    private String type;
    private String description;

    StatusType(int statusCode, String type, String description) {
        this.statusCode = statusCode;
        this.type = type;
        this.description = description;
    }

    public int getStatusCode(){
        return this.statusCode;
    }

    public String getType(){
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }
}
