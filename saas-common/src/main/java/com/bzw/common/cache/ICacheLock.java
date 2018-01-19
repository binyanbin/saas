package com.bzw.common.cache;

/**
 *
 * @author yanbin
 * @date 2017/7/10
 */
public interface ICacheLock {

    /**
     * 锁
     * @param key 关键字
     * @param waitTime 等待时间
     * @param expire 超时时间
     * @return 是否获得锁
     * @throws InterruptedException
     */
    boolean lock(String key, int waitTime, int expire) throws InterruptedException;

    /**
     * 解锁
     * @param key 关键字
     * @return 解锁是否成功
     */
    boolean unlock(String key);
}
