package com.example.controller;

import com.example.common.Result;
import com.example.service.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 计数器控制器（题目1示例）
 *
 * @author Example
 * @date 2024
 */
@RestController
@RequestMapping("/api/counter")
@RequiredArgsConstructor
public class CounterController {

    private final CounterService counterService;

    /**
     * 增加计数
     */
    @PostMapping("/{key}/increment")
    public Result<Long> increment(@PathVariable String key) {
        Long value = counterService.increment(key);
        return Result.success(value);
    }

    /**
     * 减少计数
     */
    @PostMapping("/{key}/decrement")
    public Result<Long> decrement(@PathVariable String key) {
        Long value = counterService.decrement(key);
        return Result.success(value);
    }

    /**
     * 按步长增加计数
     */
    @PostMapping("/{key}/increment/{delta}")
    public Result<Long> incrementBy(@PathVariable String key, @PathVariable Long delta) {
        Long value = counterService.incrementBy(key, delta);
        return Result.success(value);
    }

    /**
     * 获取当前计数
     */
    @GetMapping("/{key}")
    public Result<Long> getCount(@PathVariable String key) {
        Long value = counterService.getCount(key);
        return Result.success(value);
    }

    /**
     * 重置计数
     */
    @DeleteMapping("/{key}")
    public Result<Void> reset(@PathVariable String key) {
        counterService.reset(key);
        return Result.success();
    }
}
