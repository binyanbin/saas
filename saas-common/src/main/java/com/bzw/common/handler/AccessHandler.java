package com.bzw.common.handler;

import com.bzw.common.content.*;
import com.bzw.common.enums.Constants;
import com.bzw.common.exception.api.InvalidAccessTokenException;
import com.bzw.common.exception.api.InvalidSignException;
import com.bzw.common.log.LogServiceImpl;
import com.bzw.common.log.model.TimeoutInfo;
import com.bzw.common.utils.Sha256;
import com.bzw.common.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author yanbin
 */
@Component
public class AccessHandler implements HandlerInterceptor {

    private WebSessionManager webSessionManager;

    private LogServiceImpl logServiceImpl;

    private BeanFactory beanFactory;

    private long beginTime;
    private static String SIGN_KEY = "sign";

    @Autowired
    public AccessHandler(WebSessionManager webSessionManager, LogServiceImpl logServiceImpl, BeanFactory beanFactory) {
        this.webSessionManager = webSessionManager;
        this.logServiceImpl = logServiceImpl;
        this.beanFactory = beanFactory;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        beginTime = System.currentTimeMillis();
        MDC.put("ip", WebUtils.Http.getIpAddr(request));
        String userAgent = WebUtils.Http.getUserAgent(request);
        MDC.put("profile", null == userAgent ? "EmptyUserAgent" : userAgent);
        if (StringUtils.isBlank(request.getRequestURI())) {
            MDC.put("url", "none");
        } else {
            String url = request.getRequestURI();
            if (StringUtils.isNotBlank(request.getQueryString())) {
                url = url + "?" + request.getQueryString();
            }
            MDC.put("url", url);
        }
        MDC.put("method", WebUtils.Http.getMethod(request));
        WebContext webContext = buildWebContext(request, response, beanFactory);
        boolean nonSessionValidation = false;
        boolean nonSignValidaion = false;
        if (handler instanceof HandlerMethod) {
            ApiMethodAttribute methodAttribute = ((HandlerMethod) handler).getMethod().getAnnotation(ApiMethodAttribute.class);
            if (null != methodAttribute) {
                nonSessionValidation = methodAttribute.nonSessionValidation();
                nonSignValidaion = methodAttribute.nonSignatureValidation();
                webContext.setMethodAttribute(methodAttribute);
            }
        }
        if (!request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            WebSession webSession = validationSession(request, webContext, nonSessionValidation);

            if (!nonSignValidaion && !validSign(request, nonSessionValidation)) {
                throw new InvalidSignException();
            }
            loggerUserInfo(webSession);
        }
        String sessionId = WebUtils.Session.getSessionId(request);
        if (!StringUtils.isBlank(sessionId)) {
            MDC.put("sessionId", sessionId);
        }
        logServiceImpl.insertDaily();
        return true;
    }

    private WebContext buildWebContext(HttpServletRequest request,
                                       HttpServletResponse response,
                                       BeanFactory beanFactory) {
        WebContext webContext = new WebContext();
        webContext.setRequest(request);
        webContext.setResponse(response);
        webContext.setBeanFactory(beanFactory);
        ThreadWebContextHolder.setContext(webContext);
        return webContext;
    }

    private WebSession validationSession(HttpServletRequest request,
                                         WebContext webContext, boolean nonSessionValidation) {
        WebSession webSession = null;
        if (!nonSessionValidation) {
            String sessionId = WebUtils.Session.getSessionId(request);
            if (StringUtils.isBlank(sessionId)) {
                throw new InvalidAccessTokenException();
            }
            webSession = webSessionManager.get(sessionId);
            if (null == webSession || !sessionId.equals(webSession.getId())) {
                throw new InvalidAccessTokenException();
            }
        }
        if (null != webSession) {
            webContext.setWebSession(webSession);
        }
        return webSession;
    }

    private void loggerUserInfo(WebSession webSession) {
        if (null != webSession) {
            if (null != webSession.getUserId()) {
                MDC.put("userId", null == webSession.getUserId() ? ""
                        : webSession.getUserId().toString());
            }
            if (null != webSession.getName()) {
                MDC.put("name", webSession.getName());
            }
            if (null != webSession.getEmployeeId()) {
                MDC.put("employeeId", null == webSession.getEmployeeId() ? ""
                        : webSession.getEmployeeId().toString());
            }
        }
    }

    private boolean validSign(HttpServletRequest request, boolean nonSessionValidation) {
        if (StringUtils.isBlank(request.getParameter(SIGN_KEY))) {
            return false;
        }
        List<String> list = Collections.list(request.getParameterNames());
        list.remove(SIGN_KEY);
        list.sort(Comparator.naturalOrder());
        StringBuilder url = new StringBuilder();
        url.append(request.getRequestURI());
        url.append("?");
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i);
            String value = request.getParameter(key);
            url.append(key);
            url.append("=");
            url.append(value);
            if (i < list.size() - 1) {
                url.append("&");
            }
        }
        String sessionId = WebUtils.Session.getSessionId(request);
        if (StringUtils.isBlank(sessionId)) {
            throw new InvalidAccessTokenException();
        }
        if (!nonSessionValidation) {
            WebSession webSession = webSessionManager.get(sessionId);
            url.append(webSession.getSecretKey());
        }
        String sign = Sha256.encrypt(url.toString());
        return sign.equals(request.getParameter(SIGN_KEY));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String accept = request.getHeader("Accept");
        if (MediaType.APPLICATION_JSON_VALUE.equals(accept) || MediaType.ALL_VALUE.equals(accept)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {
        Method method = ((HandlerMethod) handler).getMethod();
        long endTime = System.currentTimeMillis();
        long timeSpan = 2000L;
        long time = endTime - beginTime;
        if (time > timeSpan) {
            TimeoutInfo visit = new TimeoutInfo();
            if (org.slf4j.MDC.get(Constants.IP) != null) {
                visit.setIp(org.slf4j.MDC.get(Constants.IP));
            }
            if (null != org.slf4j.MDC.get(Constants.USER_ID)) {
                visit.setUserId(org.slf4j.MDC.get(Constants.USER_ID));
            }
            if (null != org.slf4j.MDC.get(Constants.URL)) {
                visit.setUrl(org.slf4j.MDC.get(Constants.URL));
            }
            if (null != org.slf4j.MDC.get(Constants.URL_BODY)) {
                visit.setBody(org.slf4j.MDC.get(Constants.URL_BODY));
            }
            visit.setMethod(method.getName());
            visit.setTimeSpan(time);
            logServiceImpl.insertVisit(visit);
        }
    }
}
