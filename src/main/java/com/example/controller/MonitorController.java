package com.example.controller;

import com.example.common.Result;
import com.example.service.ThreadPoolMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 监控控制器（题目8示例）
 *
 * @author Example
 * @date 2024
 */
@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final ThreadPoolMonitorService monitorService;

    /**
     * 获取线程池状态
     */
    @GetMapping("/thread-pool")
    public Result<Map<String, Object>> getThreadPoolStatus() {
        Map<String, Object> status = monitorService.getThreadPoolStatus();
        return Result.success(status);
    }

    /**
     * 线程池健康检查
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> healthCheck() {
        Map<String, Object> health = monitorService.healthCheck();
        return Result.success(health);
    }
}
