package com.bzw.common.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author yanbin
 * @date 2017/7/1
 */
public enum CacheKeyPrefix {
    /**
     * redis缓存关键字
     */
    DuplicateSubmission("dup_sub_", "重复提交验证", TimeUnit.SECONDS.toSeconds(30)),
    UserSession("user_session_", "用户Session", TimeUnit.DAYS.toSeconds(7)),
    ApiSign("api_sign_", "api接口签名", TimeUnit.MINUTES.toSeconds(15)),
    Lock("lock_", "锁", TimeUnit.MINUTES.toSeconds(5)),
    mobile("mobile_code_","验证码",TimeUnit.MINUTES.toSeconds(15)),;

    CacheKeyPrefix(String value, String desc, long timeout) {
        this.key = value;
        this.desc = desc;
        this.timeout = timeout;
    }

    private String key;

    private String desc;

    private long timeout;

    /**
     * @return the value
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
