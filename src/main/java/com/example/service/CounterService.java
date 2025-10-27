package com.example.service;

import com.example.config.RedisConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 计数器服务（题目1示例）
 *
 * @author Example
 * @date 2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CounterService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisConfig redisConfig;

    /**
     * 增加计数
     */
    public Long increment(String key) {
        String fullKey = buildKey(key);
        Long result = redisTemplate.opsForValue().increment(fullKey);
        log.info("计数器 {} 递增，当前值：{}", fullKey, result);
        return result;
    }

    /**
     * 减少计数
     */
    public Long decrement(String key) {
        String fullKey = buildKey(key);
        Long result = redisTemplate.opsForValue().decrement(fullKey);
        log.info("计数器 {} 递减，当前值：{}", fullKey, result);
        return result;
    }

    /**
     * 按指定步长增加
     */
    public Long incrementBy(String key, Long delta) {
        String fullKey = buildKey(key);
        Long result = redisTemplate.opsForValue().increment(fullKey, delta);
        log.info("计数器 {} 增加 {}，当前值：{}", fullKey, delta, result);
        return result;
    }

    /**
     * 获取当前计数
     */
    public Long getCount(String key) {
        String fullKey = buildKey(key);
        Object value = redisTemplate.opsForValue().get(fullKey);
        if (value == null) {
            return 0L;
        }
        return Long.valueOf(value.toString());
    }

    /**
     * 重置计数
     */
    public void reset(String key) {
        String fullKey = buildKey(key);
        redisTemplate.opsForValue().set(fullKey, 0);
        log.info("计数器 {} 已重置为 0", fullKey);
    }

    /**
     * 设置计数并指定过期时间
     */
    public void setWithExpire(String key, Long value, Long timeout, TimeUnit timeUnit) {
        String fullKey = buildKey(key);
        redisTemplate.opsForValue().set(fullKey, value, timeout, timeUnit);
        log.info("设置计数器 {} = {}，过期时间：{} {}", fullKey, value, timeout, timeUnit);
    }

    /**
     * 构建完整的 Redis Key
     */
    private String buildKey(String key) {
        return redisConfig.getKeyPrefix() + "counter:" + key;
    }
}
