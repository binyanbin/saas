package com.bzw.common.log;

import com.bzw.common.content.WebSession;
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
 * Created by yanbin on 2017/7/7.
 */
@Component
public class LogService implements ILogService {

    private  MongoTemplate  mongoTemplate;

    @Autowired
    public LogService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public void insertExcept(JsonExceptionWrapper exception) {
        ExceptionInfo data = new ExceptionInfo();
        data.setCode(exception.getCode());
        data.setMsg(exception.getMsg());
        data.setException(exception.getStackTrace());
        if (MDC.get("profile") != null) {
            data.setProfile(MDC.get("profile"));
        }
        WebSession session = WebUtils.Session.get();
        if (MDC.get("ip") != null) {
            data.setIp(MDC.get("ip"));
        }
        if (MDC.get("url") != null) {
            data.setUrl(MDC.get("url"));
        }
        if (MDC.get("url_body") != null) {
            data.setBody(MDC.get("url_body"));
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
        if (!StringUtils.isBlank(MDC.get("sessionId"))) {
            data.setSessionId(MDC.get("sessionId"));
        }
        mongoTemplate.insert(data);
    }

    public void insertVisit(TimeoutInfo data) {

        Date tempDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(tempDate);
        data.setCreatedTime(dateString);
        format = new SimpleDateFormat("yyyy-MM-dd");
        String dayString = format.format(tempDate);
        data.setDay(dayString);
        if (!StringUtils.isBlank(MDC.get("sessionId"))) {
            data.setSessionId(MDC.get("sessionId"));
        }
        try {
            mongoTemplate.insert(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertDaily() {

        Daily data = new Daily();
        Date tempDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(tempDate);
        data.setCreatedTime(dateString);
        format = new SimpleDateFormat("yyyy-MM-dd");
        String dayString = format.format(tempDate);
        data.setDay(dayString);
        if (MDC.get("ip") != null) {
            data.setIp(MDC.get("ip"));
        }
        if (MDC.get("profile") != null)
            data.setUserAgent(MDC.get("profile"));
        if (MDC.get("method") != null)
            data.setMethod(MDC.get("method"));
        if (MDC.get("url") != null)
            data.setUrl(MDC.get("url"));
        if (MDC.get("userId") != null)
            data.setUserId(MDC.get("userId"));
        if (!StringUtils.isBlank(MDC.get("sessionId"))) {
            data.setSessionId(MDC.get("sessionId"));
        }
        mongoTemplate.insert(data);
    }

}



