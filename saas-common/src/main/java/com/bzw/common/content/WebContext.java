package com.bzw.common.content;

import org.springframework.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * @author yanbin
 * @date 2017/7/1
 */
public class WebContext {
    private WebSession webSession;

    private Map<String, String> parameters;

    private HttpServletResponse response;

    private HttpServletRequest request;

    private ApiMethodAttribute methodAttribute;

    private BeanFactory beanFactory;

    public ApiMethodAttribute getMethodAttribute() {
        return methodAttribute;
    }

    public void setMethodAttribute(ApiMethodAttribute methodAttribute) {
        this.methodAttribute = methodAttribute;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @param request
     *            the request to set
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public WebSession getWebSession() {
        return webSession;
    }

    public void setWebSession(WebSession value) {
        webSession = value;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> value) {
        parameters = value;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
