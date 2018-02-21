package com.bzw.common.utils;

import com.bzw.common.content.ThreadWebContextHolder;
import com.bzw.common.content.WebContext;
import com.bzw.common.content.WebSession;
import com.bzw.common.system.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author yanbin
 * @date 2017/7/1
 */

public final class WebUtils {

    public static final class Http {

        public static String getIpAddr(HttpServletRequest request) {
            String ip;
            ip = request.getHeader(Constants.X_FORWARDED_FOR);
            if (isInvalidIP(ip)) {
                ip = request.getHeader(Constants.PROXY_CLIENT_IP);
            }
            if (isInvalidIP(ip)) {
                ip = request.getHeader(Constants.WL_PROXY_CLIENT_IP);
            }
            if (isInvalidIP(ip)) {
                ip = request.getRemoteAddr();
            }
            if (isInvalidIP(ip)) {
                ip = request.getHeader(Constants.X_REAL_IP);
            }
            if (isInvalidIP(ip) && null != request.getAttribute(Constants.X_REAL_IP)) {
                ip = request.getAttribute(Constants.X_REAL_IP).toString();
            }
            if (null == ip) {
                ip = "unknown";
            }
            return ip;
        }

        private static boolean isInvalidIP(String ip) {
            return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip);
        }

        public static String getBasePath(HttpServletRequest request) {
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path;
            return basePath;
        }

        public static String getBasePathNotPort(HttpServletRequest request) {
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + path;
            return basePath;
        }

        public static String getContextPath(HttpServletRequest request) {
            return request.getContextPath();
        }


        public static String getUserAgent(HttpServletRequest request) {
            return request.getHeader(Constants.USER_AGENT);
        }

        public static String getMethod(HttpServletRequest request) {
            return request.getMethod();
        }
    }


    public static final class Session {

        public static String getSessionId(HttpServletRequest request) {
            String sessionId = request.getHeader(Constants.SESSION_ID);
            if (null == sessionId) {
                Cookie[] cookies = request.getCookies();
                if ((null != cookies) && (cookies.length > 0)) {
                    for (Cookie cook : cookies) {
                        if (Constants.SESSION_ID.equals(cook.getName())) {
                            sessionId = cook.getValue();
                            break;
                        }
                    }
                }
            }
            return sessionId;
        }

        public static String getId() {
            WebSession session = get();
            assert session != null;
            return session.getId();
        }

        public static String getDeviceId(HttpServletRequest request) {
            return request.getHeader(Constants.DEVICE_ID);
        }

        public static WebSession get() {
            WebContext webContext = ThreadWebContextHolder.getContext();
            if (null != webContext) {
                return webContext.getWebSession();
            }
            return null;
        }

        public static Long getUserId() {
            WebSession session = get();
            assert session != null;
            return session.getUserId();
        }

        public static Long getEmployeeId() {
            WebSession session = get();
            assert session != null;
            return session.getEmployeeId();
        }

        public static String getSecretKey() {
            WebSession session = get();
            assert session != null;
            return session.getSecretKey();
        }

        public static Long getTenantId() {
            WebSession session = get();
            assert session != null;
            return session.getTenantId();
        }

        public static String getTenantName(){
            WebSession session = get();
            assert session != null;
            return session.getTenantName();
        }

        public static Long getBranchId(){
            WebSession session = get();
            assert session != null;
            return session.getBranchId();
        }

        public static String getBranchName(){
            WebSession session = get();
            assert session != null;
            return session.getBranchName();
        }
    }
}
