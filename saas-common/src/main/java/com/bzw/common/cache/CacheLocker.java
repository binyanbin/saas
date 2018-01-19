package com.bzw.common.cache;

/**
 * Created by yanbin on 2017/7/10.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

/**
 *
 * @author yanbin
 * @date 2017/1/10
 */
@Component
public class CacheLocker implements ICacheLock{

    /**
     * 最长时间锁为5分钟
     */
    private final static int MAX_EXPIRE_TIME = (int) CacheKeyPrefix.Lock.getTimeout();

    /**
     * 系统时间偏移量5秒，服务器间的系统时间差不可以超过5秒,避免由于时间差造成错误的解锁
     */
    private final static int OFFSET_TIME = 5;
    private final static int SECOND = 1000;

    private StringRedisTemplate springRedisTemplate;

    @Autowired
    public CacheLocker(StringRedisTemplate stringLongRedisTemplate) {
        this.springRedisTemplate = stringLongRedisTemplate;
    }

    /**
     * 锁
     *
     * @param key      key
     * @param waitTime 秒 - 最大等待时间，如果还无法获取，则直接失败
     * @param expire   秒- 锁生命周期时间
     * @return true 成功 false失败
     * @throws Exception
     */
    @Override
    public boolean lock(String key, int waitTime, int expire) throws InterruptedException {
        long start = currentTimeMillis();
        String lockKey = CacheKeyPrefix.Lock.getKey() + key;
        do {
            if (!springRedisTemplate.hasKey(lockKey)) {
                Long currentTime = System.currentTimeMillis();
                springRedisTemplate.opsForValue().set(lockKey, currentTime.toString(),
                        (expire > MAX_EXPIRE_TIME) ? MAX_EXPIRE_TIME : expire, TimeUnit.SECONDS);
                return true;
            } else { // 存在锁,并对死锁进行修复
                // 上次锁时间
                long lastLockTime = Long.parseLong(springRedisTemplate.opsForValue().get(lockKey));
                // 明确死锁,，再次设定一个合理的解锁时间让系统正常解锁
                if (System.currentTimeMillis() - lastLockTime > (expire + OFFSET_TIME) * SECOND) {
                    // 原子操作，只需要一次,【任然会发生小概率事件，多个服务同时发现死锁同时执行此行代码(并发),
                    // 为什么设置解锁时间为expire（而不是更小的时间），防止在解锁发送错乱造成新锁解锁】
                    springRedisTemplate.opsForValue().set(lockKey, "999999999", expire);
                }
            }
            if (waitTime > 0) {
                Thread.sleep(500);
            }
        }
        while (waitTime > 0 && (currentTimeMillis() - start) < waitTime * 1000);
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @return
     * @throws Exception
     */
    @Override
    public boolean unlock(String key) {
        String lockKey = CacheKeyPrefix.Lock.getKey() + key;
        springRedisTemplate.delete(lockKey);
        return true;
    }
}

