package com.bzw.common.cache;


/**
 *
 * @author yanbin
 * @date 2017/7/1
 */
public interface ICacheClient {

    /**
     * 获得缓存值
     * @param key 关键字
     * @return 值
     */
    String get(String key);

    /**
     * 获得缓存值并刷新过期时间
     * @param key 关键字
     * @param expires 过期时间
     * @return 值
     */
    String getAndTouch(String key, int expires);

    /**
     * 设置缓存值
     * @param key 关键字
     * @param value 值
     * @param expires 过期时间
     */
    void set(String key, String value, int expires);

    /**
     * 设置缓存值 不过期
     * @param key 关键字
     * @param value 值
     */
    void set(String key, String value);

    /**
     * 刷新缓存过期时间
     * @param key 关键字
     * @param expires 过期时间
     */
    void touch(String key, int expires);

    /**
     * 删除缓存
     * @param key 关键字
     */
    void delete(String key);

    /**
     * 删除匹配缓存关键字
     * @param prefix 前缀
     */
    void deleteKeys(String prefix);

    /**
     * 判断是否有缓存
     * @param key 关键字
     * @return 是否存在
     */
    boolean hasKey(String key);

}
