package com.bzw.common.handler;

import com.bzw.common.cache.CacheKeyPrefix;
import com.bzw.common.cache.RedisClient;
import com.bzw.common.content.*;
import com.bzw.common.system.Constants;
import com.bzw.common.exception.api.DuplicationException;
import com.bzw.common.exception.api.InvalidAccessTokenException;
import com.bzw.common.exception.api.InvalidSignException;
import com.bzw.common.log.LogServiceImpl;
import com.bzw.common.log.model.TimeoutInfo;
import com.bzw.common.utils.DtUtils;
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
import java.util.Date;
import java.util.List;

/**
 * @author yanbin
 */
@Component
public class AccessHandler implements HandlerInterceptor {

    private WebSessionManager webSessionManager;

    private LogServiceImpl logServiceImpl;

    private BeanFactory beanFactory;

    private RedisClient redisClient;

    private long beginTime;

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

    @Autowired
    public AccessHandler(WebSessionManager webSessionManager, LogServiceImpl logServiceImpl, BeanFactory beanFactory, RedisClient redisClient) {
        this.webSessionManager = webSessionManager;
        this.logServiceImpl = logServiceImpl;
        this.beanFactory = beanFactory;
        this.redisClient = redisClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        beginTime = System.currentTimeMillis();
        MDC.put(Constants.IP, WebUtils.Http.getIpAddr(request));
        String userAgent = WebUtils.Http.getUserAgent(request);
        MDC.put(Constants.PROFILE, null == userAgent ? "EmptyUserAgent" : userAgent);
        if (StringUtils.isBlank(request.getRequestURI())) {
            MDC.put(Constants.URL, "none");
        } else {
            String url = request.getRequestURI();
            if (StringUtils.isNotBlank(request.getQueryString())) {
                url = url + "?" + request.getQueryString();
            }
            MDC.put(Constants.URL, url);
        }
        MDC.put(Constants.METHOD, WebUtils.Http.getMethod(request));
        boolean validSession = false;
        boolean validSign = false;
        boolean validDuplication = false;
        if (handler instanceof HandlerMethod) {
            WebContext webContext = buildWebContext(request, response, beanFactory);
            ApiMethodAttribute methodAttribute = ((HandlerMethod) handler).getMethod().getAnnotation(ApiMethodAttribute.class);
            if (null != methodAttribute) {
                validSession = !methodAttribute.nonSessionValidation();
                validSign = !methodAttribute.nonSignatureValidation();
                webContext.setMethodAttribute(methodAttribute);
            }
            DuplicationSubmit duplicationSubmit = ((HandlerMethod) handler).getMethod().getAnnotation(DuplicationSubmit.class);
            if (null != duplicationSubmit) {
                validDuplication = true;
            }
            if (!request.getMethod().equals(RequestMethod.OPTIONS.name())) {
                WebSession webSession = null;
                if (validSession) {
                    webSession = validationSession(request, webContext);
                }
                if (validSign) {
                    validSign(request, validSession);
                }
                if (validDuplication && webSession != null) {
                    validDuplicationSubmit(duplicationSubmit, webSession);
                }
                loggerUserInfo(webSession);
            }
        }
        String sessionId = WebUtils.Session.getSessionId(request);
        if (!StringUtils.isBlank(sessionId)) {
            MDC.put(Constants.SESSION_ID, sessionId);
        }
        logServiceImpl.insertDaily();
        return true;
    }

    /**
     * 重复提交
     */
    private void validDuplicationSubmit(DuplicationSubmit duplicationSubmit, WebSession webSession) {
        String cacheKey = CacheKeyPrefix.DuplicateSubmission.getKey() + webSession.getId() + "_" + duplicationSubmit.businessType().getValue();
        if (redisClient.hasKey(cacheKey)) {
            throw new DuplicationException();
        } else {
            redisClient.set(cacheKey, DtUtils.toDateString(new Date()),Constants.AVOID_DUPLICATE_TIME);
        }
    }

    /**
     * session
     */
    private WebSession validationSession(HttpServletRequest request,
                                         WebContext webContext) {
        WebSession webSession;
        String sessionId = WebUtils.Session.getSessionId(request);
        if (StringUtils.isBlank(sessionId)) {
            throw new InvalidAccessTokenException();
        }
        webSession = webSessionManager.get(sessionId);
        if (null == webSession || !sessionId.equals(webSession.getId())) {
            throw new InvalidAccessTokenException();
        }
        webContext.setWebSession(webSession);
        return webSession;
    }

    /**
     * 签名
     */
    private void validSign(HttpServletRequest request, boolean validSession) {
        if (StringUtils.isBlank(request.getParameter(Constants.SIGN_KEY))) {
            throw new InvalidSignException();
        }
        List<String> list = Collections.list(request.getParameterNames());
        list.remove(Constants.SIGN_KEY);
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
        if (validSession) {
            WebSession webSession = webSessionManager.get(sessionId);
            url.append(webSession.getSecretKey());
        }
        String sign = Sha256.encrypt(url.toString());
        assert sign != null;
        if (!sign.equals(request.getParameter(Constants.SIGN_KEY))){
            throw new InvalidSignException();
        }
    }

    private void loggerUserInfo(WebSession webSession) {
        if (null != webSession) {
            if (null != webSession.getUserId()) {
                MDC.put(Constants.USER_ID, null == webSession.getUserId() ? ""
                        : webSession.getUserId().toString());
            }
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String accept = request.getHeader(Constants.ACCEPT);
        if (MediaType.APPLICATION_JSON_VALUE.equals(accept) || MediaType.ALL_VALUE.equals(accept)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {
        Method method = ((HandlerMethod) handler).getMethod();
        long endTime = System.currentTimeMillis();
        long timeSpan = Constants.OVER_TIME;
        long time = endTime - beginTime;
        if (time > timeSpan) {
            TimeoutInfo visit = new TimeoutInfo();
            if (MDC.get(Constants.IP) != null) {
                visit.setIp(MDC.get(Constants.IP).toString());
            }
            if (null != MDC.get(Constants.USER_ID)) {
                visit.setUserId(MDC.get(Constants.USER_ID).toString());
            }
            if (null != MDC.get(Constants.URL)) {
                visit.setUrl(MDC.get(Constants.URL).toString());
            }
            if (null != MDC.get(Constants.URL_BODY).toString()) {
                visit.setBody(MDC.get(Constants.URL_BODY).toString());
            }
            visit.setMethod(method.getName());
            visit.setTimeSpan(time);
            logServiceImpl.insertVisit(visit);
        }
        ThreadWebContextHolder.removeContext();
    }
}
