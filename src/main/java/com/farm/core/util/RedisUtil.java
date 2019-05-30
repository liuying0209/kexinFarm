package com.farm.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 ** @Date: 2019-1-15 9:59
 */
@Component
public class RedisUtil {

    @Autowired
    RedisTemplate redisTemplate;

    @Value("${spring.redis.keyPre}")
    private String keyPre;


    public RedisUtil() {
    }

    /**
     * 设置 String 类型 key-value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(keyPre + key, value);

    }

    /**
     * 设置 通用 key-value
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(keyPre + key, value);
    }

    /**
     * 获取 String 类型 key-value
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(keyPre + key);
    }


    /**
     * 设置 String 类型 key-value 并添加过期时间 (毫秒单位)
     *
     * @param key
     * @param value
     * @param time  过期时间,毫秒单位
     */
    public void setForTimeMS(String key, String value, long time) {
        redisTemplate.opsForValue().set(keyPre + key, value, time, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     *
     * @param key
     * @param value
     * @param time  过期时间,分钟单位
     */
    public void setForTimeMIN(String key, String value, long time) {
        redisTemplate.opsForValue().set(keyPre + key, value, time, TimeUnit.MINUTES);
    }


    /**
     * 设置 String 类型 key-value 并添加过期时间 自定义单位
     *
     * @param key
     * @param value
     * @param time  过期时间,自定义单位
     */
    public void setForTimeCustom(String key, String value, long time, TimeUnit type) {
        redisTemplate.opsForValue().set(keyPre + key, value, time, type);
    }


    /**
     * 如果 key 存在则覆盖,并返回旧值.
     * 如果不存在,返回null 并添加
     *
     * @param key
     * @param value
     * @return
     */
    public String getAndSet(String key, String value) {
        return (String) redisTemplate.opsForValue().getAndSet(keyPre + key, value);
    }


    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key
     * @param number
     */
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(keyPre + key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是 纯数字 ,将报错
     *
     * @param key
     * @param number
     */
    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(keyPre + key, number);
    }


    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key
     * @param time
     * @param type
     * @return
     */
    public boolean expire(String key, long time, TimeUnit type) {
        return redisTemplate.boundValueOps(keyPre + key).expire(time, type);
    }

    /**
     * 移除指定key 的过期时间
     *
     * @param key
     * @return
     */
    public boolean persist(String key) {
        return redisTemplate.boundValueOps(keyPre + key).persist();
    }

    /**
     * 修改 key
     *
     * @param key
     * @return
     */
    public void rename(String key, String newKey) {
        redisTemplate.boundValueOps(keyPre + key).rename(newKey);
    }

    /**
     * 删除 key-value
     *
     * @param key
     * @return
     */
    public void delete(String key) {
        redisTemplate.delete(keyPre + key);
    }


}
