# Redis 和 线程池 练习题目

## 目录
- [Redis 练习题目](#redis-练习题目)
- [线程池练习题目](#线程池练习题目)
- [综合练习题目](#综合练习题目)

---

## Redis 练习题目

### 题目1：基础字符串操作（入门）
**难度：⭐**

#### 题目描述
实现一个简单的计数器服务，支持以下功能：
- 增加计数（原子操作）
- 减少计数（原子操作）
- 获取当前计数
- 重置计数

#### 实现要求
- 使用 Redis 的 `INCR`、`DECR`、`GET`、`SET` 命令
- 设计 RESTful API 接口
- 返回统一的响应格式

#### 解题思路
1. **核心数据结构**：使用 Redis String 类型存储计数器
2. **关键命令**：
   - `INCR key`: 原子性递增，避免并发问题
   - `DECR key`: 原子性递减
   - `GET key`: 获取当前值
   - `SET key 0`: 重置为 0
3. **实现步骤**：
   ```
   步骤1: 创建 CounterController，定义4个接口
   步骤2: 创建 CounterService，封装 Redis 操作
   步骤3: 使用 RedisTemplate 执行 Redis 命令
   步骤4: 统一异常处理和响应格式
   ```

#### 扩展练习
- 添加有效期限制
- 支持指定增减的步长
- 添加计数器的最大/最小值限制

---

### 题目2：Hash 结构应用（基础）
**难度：⭐⭐**

#### 题目描述
实现一个用户信息缓存服务：
- 存储用户的基本信息（ID、姓名、邮箱）
- 支持单个字段更新
- 支持获取单个字段或全部字段
- 设置缓存过期时间

#### 实现要求
- 使用 Redis Hash 类型
- 单字段操作使用 `HSET`、`HGET`
- 批量操作使用 `HMSET`、`HGETALL`
- 设置过期时间

#### 解题思路
1. **选择数据结构**：Redis Hash 适合存储对象
   - Key: user:{userId}
   - Field: name, email, status 等
   - Value: 对应的值
2. **关键命令**：
   ```redis
   HMSET learning:user:1001 name "张三" email "zhangsan@example.com"
   HGET learning:user:1001 name
   HGETALL learning:user:1001
   EXPIRE learning:user:1001 3600
   ```
3. **实现要点**：
   - Hash 不支持嵌套结构
   - 过期时间设置在 Hash Key 上
   - 批量更新时要注意事务

---

### 题目3：List 队列实现（中级）
**难度：⭐⭐⭐**

#### 题目描述
实现一个消息队列，支持生产者-消费者模式：
- 生产者：添加消息到队列尾部
- 消费者：从队列头部消费消息（阻塞式）
- 查询队列长度
- 查看队列中的所有消息

#### 实现要求
- 使用 Redis List 类型
- 实现阻塞式消费（BLPOP）
- 设置超时时间
- 处理空队列情况

#### 解题思路
1. **数据结构设计**：
   - Key: message:queue
   - Value: 消息内容（JSON 格式）
2. **关键命令**：
   ```redis
   RPUSH learning:message:queue '{"id":1,"content":"hello"}'
   BLPOP learning:message:queue 10  # 阻塞10秒
   LLEN learning:message:queue
   LRANGE learning:message:queue 0 -1
   ```
3. **实现模式**：
   - 生产者：RPUSH（右侧推入）
   - 消费者：BLPOP（左侧弹出，阻塞）
4. **注意事项**：
   - 阻塞时间设置要合理
   - 考虑多消费者并发场景
   - 消息体建议使用 JSON 便于解析

---

### 题目4：Set 去重统计（中级）
**难度：⭐⭐⭐**

#### 题目描述
实现一个 IP 访问统计和去重服务：
- 记录每日访问的 IP 地址（自动去重）
- 统计总访问量（去重后）
- 判断 IP 是否今日访问过
- 获取今日访问的 IP 列表

#### 实现要求
- 使用 Redis Set 类型
 SearsSet 集合操作
- 设置按日过期的键

#### 解题思路
1. **数据结构**：
   - Key: daily:ip:{date}，如 daily:ip:20240101
   - Value: IP 地址集合
2. **关键命令**：
   ```redis
   SADD learning:daily:ip:20240101 192.168.1.1
   SCARD learning:daily:ip:20240101  # 统计数量
   SISMEMBER learning:daily:ip:20240101 192.168.1.1  # 判断成员
   SMEMBERS learning:daily:ip:20240101  # 获取所有成员
   ```
3. **实现要点**：
   - Set 自动去重特性
   - 使用 SCARD 实现 O(1) 复杂度统计
   - 设置键过期避免无限增长

---

### 题目5：Sorted Set 排行榜（高级）
**难度：⭐⭐⭐⭐**

#### 题目描述
实现一个游戏排行榜系统：
- 用户完成某个任务获得分数
- 实时更新排行榜
- 查询用户排名
- 查询 Top N 用户
- 查询指定分数范围的用户

#### 实现要求
- 使用 Redis Sorted Set（ZSet）
- 支持按分数排序
- 支持范围查询

#### 解题思路
1. **数据结构**：
   - Key: leaderboard:game
   - Score: 用户分数
   - Member: 用户 ID
2. **关键命令**：
   ```redis
   ZADD learning:leaderboard:game 1000 user1001
   ZADD learning:leaderboard:game 1500 user1001  # 更新分数
   ZRANK learning:leaderboard:game user1001  # 获取排名（从0开始）
   ZREVRANK learning:leaderboard:game user1001  # 反向排名
   ZREVRANGE learning:leaderboard:game 0 9 WITHSCORES  # Top 10
   ZCOUNT learning:leaderboard:game 1000 2000  # 分数范围统计
   ```
3. **实现要点**：
   - ZADD 的 INCR 模式可实现原子性增减分
   - ZRANK 获取正向排名，ZREVRANK 获取反向排名
   - WITHSCORES 选项同时返回分数

---

### 题目6：分布式锁实现（高级）
**难度：⭐⭐⭐⭐⭐**

#### 题目描述
实现一个分布式锁，用于防止多实例并发执行同一任务：
- 获取锁（超时时间）
- 释放锁
- 锁续期（看门狗机制）
- 防止误删锁（仅锁的拥有者能删除）

#### 实现要求
- 使用 Redis String + SET NX EX 命令
- 实现锁续期机制
- 线程安全的锁操作
- 考虑异常情况下的锁释放

#### 解题思路
1. **核心命令**：
   ```redis
   SET learning:lock:task {lockId} NX EX 30
   # NX: 只有当 key 不存在时才设置
   # EX: 设置过期时间（秒）
   ```
2. **释放锁**：
   ```lua
   -- Lua 脚本保证原子性
   if redis.call("get", KEYS[1]) == ARGV[1] then
       return redis.call("del", KEYS[1])
   else
       return 0
   end
   ```
3. **看门狗机制**：
   - 后台线程定期续期
   - 续期间隔 = 过期时间 / 3
   - 持有锁才续期
4. **实现要点**：
   - UUID 标识锁的所有者
   - Lua 脚本保证原子性
   - 异常情况下也能正确释放

---

## 线程池练习题目

### 题目7：基本线程池使用（入门）
**难度：⭐**

#### 题目描述
实现一个批量文件处理服务：
- 处理文件夹下的所有图片
- 使用线程池并行处理
- 显示处理进度
- 汇总处理结果

#### 实现要求
- 使用 @Async 异步注解
- 配置线程池参数
- 获取执行结果（CompletableFuture）

#### 解题思路
1. **任务拆分**：
   - 扫描文件目录
   - 将每个文件处理封装成独立任务
2. **线程池配置**：
   - 核心线程数：CPU 核心数
   - 最大线程数：核心数 * 2
   - 队列：有界队列防止内存溢出
3. **实现步骤**：
   ```java
   @Async("customTaskExecutor")
   CompletableFuture<ProcessResult> processImage(String filePath);
   ```
4. **结果收集**：
   - 使用 CompletableFuture.allOf() 等待所有任务
   - 使用 join() 阻塞获取结果

---

### 题目8：线程池监控（基础）
**难度：⭐⭐**

#### 题目描述
实现一个线程池监控服务：
- 实时查看线程池状态
- 统计任务执行情况（成功、失败、总数）
- 提供线程池健康检查接口

#### 实现要求
- 监听线程池状态变化
- 记录任务执行日志
- 集成 Spring Boot Actuator

#### 解题思路
1. **线程池指标**：
   - 核心线程数
   - 当前活跃线程数
   - 队列中待执行任务数
   - 已完成任务数
2. **监控实现**：
   ```java
   ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncExecutor;
   int activeCount = executor.getActiveCount();
   int coreSize = executor.getCorePoolSize();
   int queueSize = executor.getQueueSize();
   ```
3. **自定义 Metrics**：
   - 实现 MeterRegistryCustomizer
   - 注册自定义指标

---

### 题目9：线程池拒绝策略实践（中级）
**难度：⭐⭐⭐**

#### 题目描述
实现不同拒绝策略的效果演示：
- AbortPolicy：抛出异常
- CallerRunsPolicy：调用者执行
- DiscardPolicy：直接丢弃
- DiscardOldestPolicy：丢弃最老任务

#### 实现要求
- 模拟线程池满载场景
- 观察每种策略的效果
- 记录被拒绝的任务

#### 解题思路
1. **触发拒绝**：
   - 快速提交大量任务
   - 任务执行时间设置较长
2. **实现要点**：
   ```java
   // AbortPolicy
   executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
   
   // 自定义拒绝策略
   executor.setRejectedExecutionHandler(new ThreadPoolExecutor.RejectedExecutionHandler() {
       @Override
       public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
           // 记录日志
           // 可选的降级处理
       }
   });
   ```

---

### 题目10：CompletableFuture 链式调用（高级）
**难度：⭐⭐⭐⭐**

#### 题目描述
实现一个数据异步处理流水线：
1. 异步获取用户信息
2. 基于用户信息异步获取订单列表
3. 并行获取商品信息和地址信息
4. 合并所有结果

#### 实现要求
- 使用 CompletableFuture 链式调用
- 实现并行和串行混合流程
- 异常处理和超时控制

#### 解题思路
1. **串行调用**：
   ```java
   CompletableFuture<User> future1 = getUserAsync(userId);
   CompletableFuture<Order> future2 = future1.thenCompose(user -> getOrderAsync(user.getId()));
   ```
2. **并行调用**：
   ```java
   CompletableFuture<Product> future3 = getProductAsync(productId);
   CompletableFuture<Address> future4 = getAddressAsync(addressId);
   CompletableFuture.allOf(future3, future4).join();
   ```
3. **结果合并**：
   ```java
   future1.thenCombine(future2, (user, order) -> {
       // 合并逻辑
   })
   ```

---

### 题目11：线程池池化技术（高级）
**难度：⭐⭐⭐⭐⭐**

#### 题目描述
实现一个线程池管理器：
- 支持创建多个不同用途的线程池
- 根据任务类型动态选择线程池
- 线程池的优雅关闭
- 线程池的动态调参

#### 实现要求
- 实现线程池工厂
- 实现线程池注册表
- 支持线程池配置热更新

#### 解题思路
1. **线程池工厂**：
   ```java
   public interface ThreadPoolFactory {
       Executor createThreadPool(String name, ThreadPoolConfig config);
       Executor getThreadPool(String name);
       void shutdown(String name);
   }
   ```
2. **线程池选择策略**：
   - CPU 密集型任务：线程数 ≈ CPU 核心数
   - IO 密集型任务：线程数 ≈ CPU 核心数 * 2
   - 混合型任务：取中间值

---

## 综合练习题目

### 题目12：Redis + 线程池实现限流器（综合）
**难度：⭐⭐⭐⭐

#### 题目描述
实现一个分布式限流服务：
- 基于滑动窗口算法
- 支持多实例部署
- 使用线程池异步更新限流数据
- 集成到 Web 请求中

#### 实现要求
- 使用 Redis ZSet 实现滑动窗口
- 使用线程池异步执行过期数据清理
- 提供注解式限流

#### 解题思路
1. **滑动窗口算法**：
   ```redis
   ZADD learning:rate:limit:user:1001 {timestamp} {requestId}
   ZREMRANGEBYSCORE learning:rate:limit:user:1001 0 {currentTimestamp-60}
   ZCARD learning:rate:limit:user:1001
   ```
2. **线程池异步清理**：
   - 定时任务清理过期数据
   - 避免阻塞主流程
3. **注解实现**：
   ```java
   @RateLimit(key = "user:#{#userId}", permits = 100, period = 60)
   public ResponseEntity<?> api(@PathVariable String userId) {
       // ...
   }
   ```

---

### 题目13：Redis + 线程池实现缓存更新（综合）
**难度：⭐⭐⭐⭐⭐

#### 题目描述
实现一个多级缓存系统：
- L1：本地缓存（Caffeine）
- L2：Redis 分布式缓存
- L3：数据库
- 使用线程池异步更新

#### 实现要求
- Cache-Aside 模式
- 异步回源更新
- 防止缓存击穿（分布式锁）

#### 解题思路
1. **读取流程**：
   - L1 → L2 → DB
   - 更新 L1 和 L2
2. **异步更新**：
   ```java
   @Async
   public void refreshCache(String key) {
       // 从 DB 查询
       // 更新 L1 和 L2
   }
   ```
3. **缓存击穿防护**：
   - 使用分布式锁
   - 第一个线程回源，其他线程等待

---

## 练习建议

### 学习路径
1. **入门阶段**（题目1-2）：
   - 掌握 Redis 基本数据类型
   - 熟悉 RedisTemplate API
2. **进阶阶段**（题目3-5）：
   - 理解常用数据结构的应用场景
   - 掌握高级命令使用
3. **高级阶段**（题目6）：
   - 理解分布式锁原理
   - 掌握 Lua 脚本使用
4. **综合应用**（题目12-13）：
   - 结合线程池解决实际问题
   - 理解缓存设计模式

### 调试技巧
- 使用 Redis CLI 查看数据：`redis-cli`
- 监控 Redis 命令：`MONITOR`
- 查看线程池状态：`/actuator/metrics`
- 使用 IDEA Redis 插件可视化调试

### 注意事项
- 所有 Redis Key 使用统一前缀
- 设置合理的过期时间
- 线程池大小根据任务类型调优
- 注意异常处理和资源释放
