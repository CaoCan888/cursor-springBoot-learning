package com.example.service;

import com.example.config.RedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 计数器服务测试
 *
 * @author Example
 * @date 2024
 */
@SpringBootTest
@TestPropertySource(properties = "spring.data.redis.host=localhost")
class CounterServiceTest {

    @Autowired
    private CounterService counterService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisConfig redisConfig;

    @Test
    void testIncrement() {
        String key = "test-increment";
        
        // 重置计数器
        counterService.reset(key);
        
        // 增加计数
        Long result1 = counterService.increment(key);
        assertEquals(1L, result1);
        
        // 再次增加
        Long result2 = counterService.increment(key);
        assertEquals(2L, result2);
        
        // 清理
        redisTemplate.delete(redisConfig.getKeyPrefix() + "counter:" + key);
    }

    @Test
    void testDecrement() {
        String key = "test-decrement";
        
        // 设置初始值
        counterService.incrementBy(key, 5L);
        assertEquals(5L, counterService.getCount(key));
        
        // 减少计数
        Long result = counterService.decrement(key);
        assertEquals(4L, result);
        
        // 清理
        redisTemplate.delete(redisConfig.getKeyPrefix() + "counter:" + key);
    }

    @Test
    void testReset() {
        String key = "test-reset";
        
        // 增加计数
        counterService.increment(key);
        counterService.increment(key);
        
        // 重置
        counterService.reset(key);
        
        // 验证重置成功
        assertEquals(0L, counterService.getCount(key));
        
        // 清理
        redisTemplate.delete(redisConfig.getKeyPrefix() + "counter:" + key);
    }
}
