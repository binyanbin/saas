package com.bzw.common.log;

import com.bzw.common.content.WebSession;
import com.bzw.common.enums.Constants;
import com.bzw.common.log.model.Daily;
import com.bzw.common.log.model.ExceptionInfo;
import com.bzw.common.log.model.TimeoutInfo;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.JsonExceptionWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author yanbin
 * @date 2017/7/7
 */
@Component
public class LogServiceImpl implements ILogService {

    private MongoTemplate mongoTemplate;

    @Autowired
    public LogServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insertExcept(JsonExceptionWrapper exception) {
        ExceptionInfo data = new ExceptionInfo();
        data.setCode(exception.getCode());
        data.setMsg(exception.getMsg());
        data.setException(exception.getStackTrace());
        if (MDC.get(Constants.PROFILE) != null) {
            data.setProfile(MDC.get(Constants.PROFILE));
        }
        WebSession session = WebUtils.Session.get();
        if (MDC.get(Constants.IP) != null) {
            data.setIp(MDC.get(Constants.IP));
        }
        if (MDC.get(Constants.URL) != null) {
            data.setUrl(MDC.get(Constants.URL));
        }
        if (MDC.get(Constants.URL_BODY) != null) {
            data.setBody(MDC.get(Constants.URL_BODY));
        }
        if (session != null) {
            if (session.getUserId() != null) {
                data.setUserId(session.getUserId().toString());
            }
            if (session.getName() != null) {
                data.setUserName(session.getName());
            }
        }
        Date tempDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(tempDate);
        data.setCreatedTime(dateString);
        format = new SimpleDateFormat("yyyy-MM-dd");
        String dayString = format.format(tempDate);
        data.setDay(dayString);
        if (!StringUtils.isBlank(MDC.get(Constants.SESSION_ID))) {
            data.setSessionId(MDC.get(Constants.SESSION_ID));
        }
        mongoTemplate.insert(data);
    }

    @Override
    public void insertVisit(TimeoutInfo data) {

        Date tempDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(tempDate);
        data.setCreatedTime(dateString);
        format = new SimpleDateFormat("yyyy-MM-dd");
        String dayString = format.format(tempDate);
        data.setDay(dayString);
        if (!StringUtils.isBlank(MDC.get(Constants.SESSION_ID))) {
            data.setSessionId(MDC.get(Constants.SESSION_ID));
        }
        try {
            mongoTemplate.insert(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertDaily() {

        Daily data = new Daily();
        Date tempDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(tempDate);
        data.setCreatedTime(dateString);
        format = new SimpleDateFormat("yyyy-MM-dd");
        String dayString = format.format(tempDate);
        data.setDay(dayString);
        if (MDC.get(Constants.IP) != null) {
            data.setIp(MDC.get(Constants.IP));
        }
        if (MDC.get(Constants.PROFILE) != null) {
            data.setUserAgent(MDC.get(Constants.PROFILE));
        }
        if (MDC.get(Constants.METHOD) != null) {
            data.setMethod(MDC.get(Constants.METHOD));
        }
        if (MDC.get(Constants.URL) != null) {
            data.setUrl(MDC.get(Constants.URL));
        }
        if (MDC.get(Constants.USER_ID) != null) {
            data.setUserId(MDC.get(Constants.USER_ID));
        }
        if (!StringUtils.isBlank(MDC.get(Constants.SESSION_ID))) {
            data.setSessionId(MDC.get("sessionId"));
        }
        mongoTemplate.insert(data);
    }

}



