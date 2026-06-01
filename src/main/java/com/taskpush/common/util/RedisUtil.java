package com.taskpush.common.util;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
        } catch (Exception e) {
            log.error("Redis set error: key={}, error={}", key, e.getMessage());
        }
    }

    /**
     * 设置缓存并指定过期时间
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value), timeout, unit);
        } catch (Exception e) {
            log.error("Redis set with timeout error: key={}, error={}", key, e.getMessage());
        }
    }

    /**
     * 获取缓存对象
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            String json = (String) redisTemplate.opsForValue().get(key);
            if (json == null) {
                return null;
            }
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("Redis get error: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 获取缓存字符串
     */
    public String getString(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            return value.toString();
        } catch (Exception e) {
            log.error("Redis getString error: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 删除缓存
     */
    public boolean delete(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.delete(key));
        } catch (Exception e) {
            log.error("Redis delete error: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 设置过期时间
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
        } catch (Exception e) {
            log.error("Redis expire error: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 判断key是否存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Redis hasKey error: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 自增
     */
    public long incr(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().increment(key, delta);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("Redis incr error: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    /**
     * 自减
     */
    public long decr(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().decrement(key, delta);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("Redis decr error: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    /**
     * 分布式锁 - 尝试获取锁
     */
    public boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        try {
            return Boolean.TRUE.equals(
                    redisTemplate.opsForValue().setIfAbsent(key, JSON.toJSONString(value), timeout, unit)
            );
        } catch (Exception e) {
            log.error("Redis setIfAbsent error: key={}, error={}", key, e.getMessage());
            return false;
        }
    }
}
