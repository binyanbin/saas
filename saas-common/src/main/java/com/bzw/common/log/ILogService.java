package com.bzw.common.log;

import com.bzw.common.log.model.TimeoutInfo;
import com.bzw.common.web.JsonExceptionWrapper;

/**
 * @author yanbin
 */
public interface ILogService {
    /**
     * 新增异常日志方法
     * @param exception 异常对像
     */
    void insertExcept(JsonExceptionWrapper exception);

    /**
     * 新增超时日志方法
     * @param data 超时日志对像
     */
    void insertVisit(TimeoutInfo data);

    /**
     * 新增访问日志方法
     */
    void insertDaily();
}
