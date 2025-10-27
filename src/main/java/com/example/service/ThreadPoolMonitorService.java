package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池监控服务（题目8示例）
 *
 * @author Example
 * @date 2024
 */
@Slf4j
@Service
public class ThreadPoolMonitorService {

    private final ThreadPoolTaskExecutor executor;

    public ThreadPoolMonitorService(@Qualifier("customTaskExecutor") ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    /**
     * 获取线程池状态信息
     */
    public Map<String, Object> getThreadPoolStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // 核心线程数
        status.put("corePoolSize", executor.getCorePoolSize());
        
        // 最大线程数
        status.put("maxPoolSize", executor.getMaxPoolSize());
        
        // 当前活跃线程数
        status.put("activeCount", executor.getActiveCount());
        
        // 队列中待执行任务数
        status.put("queueSize", executor.getQueueSize());
        
        // 已完成任务数
        status.put("completedTaskCount", getCompletedTaskCount());
        
        // 线程池总线程数（近似值）
        status.put("poolSize", executor.getPoolSize());
        
        // 队列容量
        status.put("queueCapacity", executor.getQueueCapacity());
        
        // 队列使用率
        double queueUsage = executor.getQueueCapacity() > 0 
                ? (double) executor.getQueueSize() / executor.getQueueCapacity() * 100 
                : 0;
        status.put("queueUsage", String.format("%.2f%%", queueUsage));
        
        // 线程池利用率
        double poolUsage = executor.getMaxPoolSize() > 0 
                ? (double) executor.getActiveCount() / executor.getMaxPoolSize() * 100 
                : 0;
        status.put("poolUsage", String.format("%.2f%%", poolUsage));
        
        return status;
    }

    /**
     * 线程池健康检查
     */
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        
        ThreadPoolExecutor threadPoolExecutor = executor.getThreadPoolExecutor();
        boolean isHealthy = threadPoolExecutor != null && !threadPoolExecutor.isShutdown();
        
        health.put("status", isHealthy ? "UP" : "DOWN");
        health.put("threadPoolActive", isHealthy);
        health.put("details", getThreadPoolStatus());
        
        // 检查队列是否接近满载
        int queueUsageRate = (int) ((double) executor.getQueueSize() / executor.getQueueCapacity() * 100);
        if (queueUsageRate > 90) {
            health.put("warning", "线程池队列使用率超过90%");
            health.put("status", "WARNING");
        }
        
        return health;
    }

    /**
     * 获取已完成任务数
     */
    private Long getCompletedTaskCount() {
        ThreadPoolExecutor threadPoolExecutor = executor.getThreadPoolExecutor();
        if (threadPoolExecutor != null) {
            return threadPoolExecutor.getCompletedTaskCount();
        }
        return 0L;
    }
}
