package com.bzw.common.utils;

import com.bzw.common.content.ThreadWebContextHolder;
import com.bzw.common.content.WebContext;
import com.bzw.common.content.WebSession;
import com.bzw.common.enums.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author yanbin
 * @date 2017/7/1
 */
public final class WebUtils {

    public static final class Http {
        /**
         * Get the request IP
         *
         * @param request
         * @return
         */
        public static String getIpAddr(HttpServletRequest request) {
            String ip = "0.0.0.0";
            ip = request.getHeader("x-forwarded-for");
            if (isInvalidIP(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (isInvalidIP(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (isInvalidIP(ip)) {
                ip = request.getRemoteAddr();
            }
            // get X-real-ip from nginx
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

        /**
         * Get the basic path
         *
         * @param request
         * @return
         */
        public static String getBasePath(HttpServletRequest request) {
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path;
            return basePath;
        }

        /**
         * Get the basic path haven't port
         *
         * @param request
         * @return
         */
        public static String getBasePathNotPort(HttpServletRequest request) {
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + path;
            return basePath;
        }

        /**
         * Get the context path
         *
         * @param request
         * @return
         */
        public static String getContextPath(HttpServletRequest request) {
            String path = request.getContextPath();
            return path;
        }


        public static String getUserAgent(HttpServletRequest request) {
            return request.getHeader("user-agent");
        }

        public static String getMethod(HttpServletRequest request) {
            return request.getMethod();
        }
    }


    public static final class Session {

        private static final String SESSION_ID = "sessionId";
        private static final String DEVICE_ID = "deviceId";

        /**
         * 获取会话ID。
         */
        public static String getSessionId(HttpServletRequest request) {
            String sessionId = request.getHeader(SESSION_ID);
            if (null == sessionId) {
                Cookie[] cookies = request.getCookies();
                if ((null != cookies) && (cookies.length > 0)) {
                    for (Cookie cook : cookies) {
                        if (SESSION_ID.equals(cook.getName())) {
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
            return request.getHeader(DEVICE_ID);
        }

        public static WebSession get() {
            WebContext webContext = ThreadWebContextHolder.getContext();
            if (null != webContext) {
                return webContext.getWebSession();
            }
            return null;
        }

        public static void set(WebSession session) {
            WebContext webContext = ThreadWebContextHolder.getContext();
            if (null != webContext) {
                webContext.setWebSession(session);
                ThreadWebContextHolder.setContext(webContext);
            }
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
    }
}
