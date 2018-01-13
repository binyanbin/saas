package com.bzw.common.log;

import com.bzw.common.log.model.TimeoutInfo;
import com.bzw.common.web.JsonExceptionWrapper;

public interface ILogService {
    void insertExcept(JsonExceptionWrapper exception);

    void insertVisit(TimeoutInfo data);

    void insertDaily();
}
