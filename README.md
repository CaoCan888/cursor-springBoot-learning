# SpringBoot Redis 和 线程池学习项目

这是一个用于学习和练习 Redis 和 线程池的 Spring Boot 项目。

## 项目简介

本项目提供了从入门到高级的 Redis 和 线程池练习题目，涵盖了：
- Redis 基础数据类型操作
- Redis 高级特性（分布式锁、限流等）
- 线程池配置和监控
- Redis + 线程池的综合应用

## 技术栈

- **Java 21**：使用 Record、Pattern Matching、Text Blocks 等现代特性
- **Spring Boot 3.2.0**：最新版本的 Spring Boot
- **Redis**：分布式缓存
- **Lettuce**：Redis 连接池
- **Spring Boot Actuator**：应用监控
- **Lombok**：简化代码
- **Hutool**：工具类库

## 项目结构

```
springboot-redis-threadpool-learning/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── config/              # 配置类
│   │   │   │   ├── RedisConfig.java
│   │   │   │   └── ThreadPoolConfig.java
│   │   │   ├── common/              # 通用组件
│   │   │   │   ├── Result.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── controller/          # 控制器
│   │   │   │   ├── CounterController.java
│   │   │   │   └── MonitorController.java
│   │   │   ├── service/             # 服务层
│   │   │   │   ├── CounterService.java
│   │   │   │   └── ThreadPoolMonitorService.java
│   │   │   └── SpringBootRedisThreadPoolLearningApplication.java
│   │   └── resources/
│   │       └── application.yml      # 配置文件
│   └── test/
└── EXERCISES.md                     # 练习题目文档
```

## 快速开始

### 1. 环境要求

- JDK 21+
- Maven 3.8+
- Redis 6.0+（Docker 推荐）

### 2. 启动 Redis（Docker）

```bash
docker run -d --name redis-learning -p 6379:6379 redis:7-alpine
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`，根据实际情况调整 Redis 连接信息。

### 4. 运行项目

```bash
mvn spring-boot:run
```

或者使用 IDE 直接运行 `SpringBootRedisThreadPoolLearningApplication`。

### 5. 访问接口

项目启动后，访问以下 URL 查看效果：

- **计数器 API**：`http://localhost:8080/api/counter/{key}`
- **线程池监控**：`http://localhost:8080/api/monitor/thread-pool`
- **健康检查**：`http://localhost:8080/api/monitor/health`

## 练习题目

详细题目列表和解题思路请参考 [EXERCISES.md](EXERCISES.md)。

### Redis 练习题目

| 题目 | 名称 | 难度 | 技术点 |
|------|------|------|--------|
| 1 | 基础字符串操作 | ⭐ | String、INCR、DECR |
| 2 | Hash 结构应用 | ⭐⭐ | Hash、HMSET、HGET |
| 3 | List 队列实现 | ⭐⭐⭐ | List、BLPOP、RPUSH |
| 4 | Set 去重统计 | ⭐⭐⭐ | Set、SADD、SCARD |
| 5 | Sorted Set 排行榜 | ⭐⭐⭐⭐ | ZSet、ZADD、ZRANK |
| 6 | 分布式锁实现 | ⭐⭐⭐⭐⭐ | Lua 脚本、原子操作 |

### 线程池练习题目

| 题目 | 名称 | 难度 | 技术点 |
|------|------|------|--------|
| 7 | 基本线程池使用 | ⭐ | @Async、CompletableFuture |
| 8 | 线程池监控 | ⭐⭐ | 监控指标、健康检查 |
| 9 | 拒绝策略实践 | ⭐⭐⭐ | AbortPolicy、CallerRunsPolicy |
| 10 | CompletableFuture 链式调用 | ⭐⭐⭐⭐ | thenCompose、thenCombine |
| 11 | 线程池池化技术 | ⭐⭐⭐⭐⭐ | 动态配置、优雅关闭 |

### 综合练习

| 题目 | 名称 | 难度 |
|------|------|------|
| 12 | Redis + 线程池实现限流器 | ⭐⭐⭐⭐ |
| 13 | Redis + 线程池实现缓存更新 | ⭐⭐⭐⭐⭐ |

## API 示例

### 1. 计数器操作

```bash
# 增加计数
curl -X POST http://localhost:8080/api/counter/test/increment

# 减少计数
curl -X POST http://localhost:8080/api/counter/test/decrement

# 获取计数
curl http://localhost:8080/api/counter/test

# 重置计数
curl -X DELETE http://localhost:8080/api/counter/test
```

### 2. 线程池监控

```bash
# 查看线程池状态
curl http://localhost:8080/api/monitor/thread-pool

# 健康检查
curl http://localhost:8080/api/monitor/health
```

## 配置说明

### Redis 配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      lettuce:
        pool:
          max-active: 8  # 最大连接数
          max-idle: 8    # 最大空闲连接
          min-idle: 0    # 最小空闲连接
```

### 线程池配置

```yaml
app:
  thread-pool:
    core-size: 5            # 核心线程数
    max-size: 10            # 最大线程数
    queue-capacity: 100     # 队列容量
    keep-alive-seconds: 60  # 线程存活时间
```

## 学习建议

1. **按照顺序练习**：从题目1开始，循序渐进
2. **动手实践**：每道题目都要亲自实现
3. **查阅文档**：遇到问题查阅官方文档
4. **调试技巧**：
   - 使用 Redis CLI 查看数据
   - 使用线程池监控接口观察状态
   - 查看日志了解执行流程

## 注意事项

- Redis 服务性的正常启动
- 所有 Redis Key 使用统一前缀 `learning:`
- 注意异常处理和资源释放
- 合理设置过期时间避免内存泄漏

## 参考资料

- [Redis 官方文档](https://redis.io/docs/)
- [Spring Data Redis 文档](https://docs.spring.io/spring-data/redis/docs/current/reference/html/)
- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Java 线程池最佳实践](https://docs.oracle.com/javase/tutorial/essential/concurrency/pools.html)

## 许可证

MIT License
