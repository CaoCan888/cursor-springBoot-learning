package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 *
 * @author Example
 * @date 2024
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Value("${app.thread-pool.core-size}")
    private int coreSize;

    @Value("${app.thread-pool.max-size}")
    private int maxSize;

    @Value("${app.thread-pool.queue-capacity}")
    private int queueCapacity;

    @Value("${app.thread-pool.keep-alive-seconds}")
    private int keepAliveSeconds;

    @Value("${app.thread-pool.thread-name-prefix}")
    private String threadNamePrefix;

    /**
     * 自定义线程池
     */
    @Bean(name = "customTaskExecutor")
    public ThreadPoolTaskExecutor customTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数
        executor.setCorePoolSize(coreSize);
        // 最大线程数
        executor.setMaxPoolSize(maxSize);
        // 队列容量
        executor.setQueueCapacity(queueCapacity);
        // 线程存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程名称前缀
        executor.setThreadNamePrefix(threadNamePrefix);
        
        // 拒绝策略：调用者运行策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        
        log.info("自定义线程池初始化完成 - 核心线程数: {}, 最大线程数: {}, 队列容量: {}", 
                coreSize, maxSize, queueCapacity);
        
        return executor;
    }
}
