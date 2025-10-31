package com.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RedisAndThreadPracticeTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void setUp() {
        // 清理Redis，确保每个测试都在干净的环境中运行
        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    // Redis 基础操作练习题
    @Test
    void testRedisBasicOperations() {
        String key = "mykey";
        String value = "myvalue";

        // 练习1: 设置一个键值对
        stringRedisTemplate.opsForValue().set(key, value);
        System.out.println("Set key: " + key + ", value: " + value);

        // 练习2: 获取键的值
        String retrievedValue = stringRedisTemplate.opsForValue().get(key);
        System.out.println("Get key: " + key + ", value: " + retrievedValue);
        assertEquals(value, retrievedValue);

        // 练习3: 检查键是否存在
        Boolean exists = stringRedisTemplate.hasKey(key);
        System.out.println("Key " + key + " exists: " + exists);
        assertTrue(exists);

        // 练习4: 删除键
        stringRedisTemplate.delete(key);
        System.out.println("Deleted key: " + key);

        // 练习5: 再次检查键是否存在
        exists = stringRedisTemplate.hasKey(key);
        System.out.println("Key " + key + " exists after deletion: " + exists);
        assertFalse(exists);
    }

    // Redis List 数据结构练习题
    @Test
    void testRedisListOperations() {
        String listKey = "mylist";
        String value1 = "item1";
        String value2 = "item2";
        String value3 = "item3";

        // 练习1: 向列表中从左侧推入元素
        stringRedisTemplate.opsForList().leftPush(listKey, value1);
        stringRedisTemplate.opsForList().leftPush(listKey, value2);
        System.out.println("Left push: " + value1 + ", " + value2);

        // 练习2: 获取列表所有元素
        assertEquals(2L, (long) stringRedisTemplate.opsForList().size(listKey));
        System.out.println("List size: " + stringRedisTemplate.opsForList().size(listKey));
        // 期望顺序是 [value2, value1]
        assertEquals(value2, stringRedisTemplate.opsForList().index(listKey, 0));
        assertEquals(value1, stringRedisTemplate.opsForList().index(listKey, 1));
        
        // 练习3: 向列表中从右侧推入元素
        stringRedisTemplate.opsForList().rightPush(listKey, value3);
        System.out.println("Right push: " + value3);
        // 期望顺序是 [value2, value1, value3]
        assertEquals(3L, (long) stringRedisTemplate.opsForList().size(listKey));
        assertEquals(value3, stringRedisTemplate.opsForList().index(listKey, 2));

        // 练习4: 从列表中左侧弹出元素
        String poppedValue = stringRedisTemplate.opsForList().leftPop(listKey);
        System.out.println("Left pop: " + poppedValue);
        assertEquals(value2, poppedValue);
        assertEquals(2L, (long) stringRedisTemplate.opsForList().size(listKey)); // [value1, value3]

        // 练习5: 从列表中右侧弹出元素
        poppedValue = stringRedisTemplate.opsForList().rightPop(listKey);
        System.out.println("Right pop: " + poppedValue);
        assertEquals(value3, poppedValue);
        assertEquals(1L, (long) stringRedisTemplate.opsForList().size(listKey)); // [value1]

        // 练习6: 获取列表指定范围的元素
        stringRedisTemplate.opsForList().rightPush(listKey, value2);
        stringRedisTemplate.opsForList().rightPush(listKey, value3);
        // 列表现在是 [value1, value2, value3]
        java.util.List<String> rangeList = stringRedisTemplate.opsForList().range(listKey, 0, 2); // 范围应为 0 到 2
        System.out.println("Range 0-2: " + rangeList);
        assertEquals(3, rangeList.size());
        assertEquals(value1, rangeList.get(0));
        assertEquals(value2, rangeList.get(1));
        assertEquals(value3, rangeList.get(2));
    }

    // Redis Hash 数据结构练习题
    @Test
    void testRedisHashOperations() {
        String hashKey = "myhash";
        String field1 = "name";
        String value1 = "Alice";
        String field2 = "age";
        String value2 = "30";

        // 练习1: 设置哈希字段
        stringRedisTemplate.opsForHash().put(hashKey, field1, value1);
        stringRedisTemplate.opsForHash().put(hashKey, field2, value2);
        System.out.println("Set hash fields: " + field1 + "=" + value1 + ", " + field2 + "=" + value2);

        // 练习2: 获取哈希字段的值
        String retrievedValue1 = (String) stringRedisTemplate.opsForHash().get(hashKey, field1);
        String retrievedValue2 = (String) stringRedisTemplate.opsForHash().get(hashKey, field2);
        System.out.println("Get hash fields: " + field1 + "=" + retrievedValue1 + ", " + field2 + "=" + retrievedValue2);
        assertEquals(value1, retrievedValue1);
        assertEquals(value2, retrievedValue2);

        // 练习3: 获取哈希所有字段和值
        java.util.Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(hashKey);
        System.out.println("All hash entries: " + entries);
        assertEquals(2, entries.size());
        assertEquals(value1, entries.get(field1));
        assertEquals(value2, entries.get(field2));

        // 练习4: 检查哈希字段是否存在
        Boolean fieldExists = stringRedisTemplate.opsForHash().hasKey(hashKey, field1);
        System.out.println("Field " + field1 + " exists in hash: " + fieldExists);
        assertTrue(fieldExists);

        // 练习5: 删除哈希字段
        stringRedisTemplate.opsForHash().delete(hashKey, field1);
        System.out.println("Deleted field: " + field1);
        fieldExists = stringRedisTemplate.opsForHash().hasKey(hashKey, field1);
        System.out.println("Field " + field1 + " exists after deletion: " + fieldExists);
        assertFalse(fieldExists);
    }

    // Redis Set 数据结构练习题
    @Test
    void testRedisSetOperations() {
        String setKey = "myset";
        String member1 = "apple";
        String member2 = "banana";
        String member3 = "orange";

        // 练习1: 添加元素到集合
        stringRedisTemplate.opsForSet().add(setKey, member1, member2);
        System.out.println("Add members to set: " + member1 + ", " + member2);
        assertEquals(2L, (long) stringRedisTemplate.opsForSet().size(setKey));

        // 练习2: 检查元素是否存在于集合
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(setKey, member1);
        System.out.println("Is " + member1 + " a member of set: " + isMember);
        assertTrue(isMember);

        // 练习3: 获取集合所有元素
        java.util.Set<String> members = stringRedisTemplate.opsForSet().members(setKey);
        System.out.println("All set members: " + members);
        assertTrue(members.contains(member1));
        assertTrue(members.contains(member2));

        // 练习4: 从集合中移除元素
        stringRedisTemplate.opsForSet().remove(setKey, member1);
        System.out.println("Remove member from set: " + member1);
        assertEquals(1L, (long) stringRedisTemplate.opsForSet().size(setKey));
        assertFalse(stringRedisTemplate.opsForSet().isMember(setKey, member1));

        // 练习5: 集合交集、并集、差集 (需要另一个集合)
        String setKey2 = "myset2";
        stringRedisTemplate.opsForSet().add(setKey2, member2, member3);
        System.out.println("Set2 members: " + stringRedisTemplate.opsForSet().members(setKey2));

        // 交集
        java.util.Set<String> intersection = stringRedisTemplate.opsForSet().intersect(setKey, setKey2);
        System.out.println("Intersection of " + setKey + " and " + setKey2 + ": " + intersection);
        assertEquals(1, intersection.size());
        assertTrue(intersection.contains(member2));

        // 并集
        java.util.Set<String> union = stringRedisTemplate.opsForSet().union(setKey, setKey2);
        System.out.println("Union of " + setKey + " and " + setKey2 + ": " + union);
        assertEquals(2, union.size()); // setKey 只有 member2, setKey2 只有 member2, member3
        assertTrue(union.contains(member2));
        assertTrue(union.contains(member3));
        
        // 差集 (setKey - setKey2)
        java.util.Set<String> difference = stringRedisTemplate.opsForSet().difference(setKey, setKey2);
        System.out.println("Difference of " + setKey + " and " + setKey2 + ": " + difference);
        assertEquals(0, difference.size());

    }

    // Redis ZSet (有序集合) 数据结构练习题
    @Test
    void testRedisZSetOperations() {
        String zsetKey = "myzset";
        String member1 = "userA";
        String member2 = "userB";
        String member3 = "userC";

        // 练习1: 添加元素到有序集合，并指定分数
        stringRedisTemplate.opsForZSet().add(zsetKey, member1, 10.0);
        stringRedisTemplate.opsForZSet().add(zsetKey, member2, 20.0);
        stringRedisTemplate.opsForZSet().add(zsetKey, member3, 15.0);
        System.out.println("Add members to zset: " + member1 + "(10.0), " + member2 + "(20.0), " + member3 + "(15.0)");
        assertEquals(3L, (long) stringRedisTemplate.opsForZSet().size(zsetKey));

        // 练习2: 获取指定范围的元素 (按分数从小到大)
        java.util.Set<String> rangeMembers = stringRedisTemplate.opsForZSet().range(zsetKey, 0, -1);
        System.out.println("All zset members (sorted by score ASC): " + rangeMembers);
        assertEquals(java.util.Arrays.asList(member1, member3, member2), new java.util.ArrayList<>(rangeMembers));

        // 练习3: 获取指定范围的元素 (按分数从大到小)
        java.util.Set<String> reverseRangeMembers = stringRedisTemplate.opsForZSet().reverseRange(zsetKey, 0, -1);
        System.out.println("All zset members (sorted by score DESC): " + reverseRangeMembers);
        assertEquals(java.util.Arrays.asList(member2, member3, member1), new java.util.ArrayList<>(reverseRangeMembers));

        // 练习4: 获取元素的分数
        Double score = stringRedisTemplate.opsForZSet().score(zsetKey, member1);
        System.out.println("Score of " + member1 + ": " + score);
        assertEquals(10.0, score);

        // 练习5: 增加元素的分数
        stringRedisTemplate.opsForZSet().incrementScore(zsetKey, member1, 5.0);
        score = stringRedisTemplate.opsForZSet().score(zsetKey, member1);
        System.out.println("New score of " + member1 + " after increment: " + score);
        assertEquals(15.0, score);

        // 练习6: 移除元素
        stringRedisTemplate.opsForZSet().remove(zsetKey, member2);
        System.out.println("Remove member from zset: " + member2);
        assertEquals(2L, (long) stringRedisTemplate.opsForZSet().size(zsetKey));
        assertNull(stringRedisTemplate.opsForZSet().score(zsetKey, member2));
    }

    // Redis 高级操作练习题 (INCR, EXPIRE, Pub/Sub)
    @Test
    void testRedisAdvancedOperations() throws Exception {
        String counterKey = "mycounter";
        String expiryKey = "temporaryKey";
        String expiryValue = "expiresSoon";
        String channel = "mychannel";

        // 练习1: 原子递增
        Long initialValue = stringRedisTemplate.opsForValue().increment(counterKey);
        System.out.println("Initial counter value: " + initialValue);
        assertEquals(1L, initialValue);

        Long incrementedValue = stringRedisTemplate.opsForValue().increment(counterKey, 5);
        System.out.println("Incremented counter by 5: " + incrementedValue);
        assertEquals(6L, incrementedValue);

        // 练习2: 设置键的过期时间
        stringRedisTemplate.opsForValue().set(expiryKey, expiryValue);
        stringRedisTemplate.expire(expiryKey, java.time.Duration.ofSeconds(2));
        System.out.println("Set key: " + expiryKey + ", value: " + expiryValue + ", expires in 2 seconds.");

        assertTrue(stringRedisTemplate.hasKey(expiryKey));
        Thread.sleep(2500); // 等待2.5秒，确保过期
        assertFalse(stringRedisTemplate.hasKey(expiryKey));
        System.out.println("Key " + expiryKey + " expired.");

        // 练习3: 发布/订阅 (Pub/Sub)
        // 这是一个异步操作，单元测试中模拟较为复杂，这里只展示发布，订阅需要另起线程或真实的应用场景。
        // 为了能在单元测试中验证，我们可以尝试启动一个简单的监听器。

        // 创建一个RedisMessageListenerContainer
        org.springframework.data.redis.listener.RedisMessageListenerContainer container =
                new org.springframework.data.redis.listener.RedisMessageListenerContainer();
        container.setConnectionFactory(stringRedisTemplate.getConnectionFactory());
        container.afterPropertiesSet();
        container.start(); // 启动容器

        // 创建一个阻塞队列用于接收消息
        java.util.concurrent.BlockingQueue<String> messageQueue = new java.util.concurrent.LinkedBlockingQueue<>();

        // 创建一个消息监听器
        org.springframework.data.redis.listener.adapter.MessageListenerAdapter listenerAdapter =
                new org.springframework.data.redis.listener.adapter.MessageListenerAdapter(
                        (org.springframework.data.redis.connection.MessageListener) (message, pattern) -> {
                            String receivedMessage = stringRedisTemplate.getStringSerializer().deserialize(message.getBody());
                            messageQueue.offer(receivedMessage);
                            System.out.println("Received message: " + receivedMessage + " from channel: " + stringRedisTemplate.getStringSerializer().deserialize(message.getChannel()));
                        }
                );
        
        // 添加监听器到容器
        container.addMessageListener(listenerAdapter, new org.springframework.data.redis.listener.ChannelTopic(channel));
        System.out.println("Listening on channel: " + channel);

        String message1 = "Hello Redis!";
        String message2 = "How are you?";

        // 练习4: 发布消息
        stringRedisTemplate.convertAndSend(channel, message1);
        System.out.println("Published message: " + message1 + " to channel: " + channel);
        stringRedisTemplate.convertAndSend(channel, message2);
        System.out.println("Published message: " + message2 + " to channel: " + channel);

        // 验证接收到的消息
        assertEquals(message1, messageQueue.poll(5, java.util.concurrent.TimeUnit.SECONDS));
        assertEquals(message2, messageQueue.poll(5, java.util.concurrent.TimeUnit.SECONDS));

        // 停止容器
        container.stop();
        container.destroy();
    }

    // Java 线程基础练习题
    @Test
    void testThreadBasicOperations() throws InterruptedException {
        // 练习1: 使用 Thread 类创建并启动线程
        class MyThread extends Thread {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " is running (MyThread).");
            }
        }
        MyThread thread1 = new MyThread();
        thread1.start();
        thread1.join(); // 等待线程执行完毕

        // 练习2: 使用 Runnable 接口创建并启动线程
        Runnable myRunnable = () -> {
            System.out.println(Thread.currentThread().getName() + " is running (Runnable).");
        };
        Thread thread2 = new Thread(myRunnable);
        thread2.start();
        thread2.join(); // 等待线程执行完毕

        // 练习3: 获取当前线程信息
        Thread currentThread = Thread.currentThread();
        System.out.println("Current thread name: " + currentThread.getName());
        System.out.println("Current thread ID: " + currentThread.getId());
        System.out.println("Current thread priority: " + currentThread.getPriority());
        System.out.println("Current thread state: " + currentThread.getState());

        // 练习4: 线程休眠
        long startTime = System.currentTimeMillis();
        System.out.println("Sleeping for 1 second...");
        Thread.sleep(1000);
        long endTime = System.currentTimeMillis();
        System.out.println("Woke up after " + (endTime - startTime) + " ms.");
        assertTrue((endTime - startTime) >= 1000);
    }

    // Java 线程池练习题 (ExecutorService, Callable, Future)
    @Test
    void testThreadPoolOperations() throws InterruptedException, java.util.concurrent.ExecutionException {
        // 练习1: 创建固定大小的线程池
        java.util.concurrent.ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(2);
        System.out.println("Created a fixed thread pool with 2 threads.");

        // 练习2: 提交 Runnable 任务
        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " executing Runnable task 1.");
        });
        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " executing Runnable task 2.");
        });

        // 练习3: 提交 Callable 任务并获取结果
        java.util.concurrent.Future<String> future = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " executing Callable task.");
            Thread.sleep(500);
            return "Task completed by " + Thread.currentThread().getName();
        });

        System.out.println("Submitted Callable task, waiting for result...");
        String result = future.get(); // 阻塞直到任务完成
        System.out.println("Callable task result: " + result);
        assertTrue(result.startsWith("Task completed by"));

        // 练习4: 关闭线程池
        executorService.shutdown();
        System.out.println("Shutting down thread pool.");
        // 等待所有任务完成
        assertTrue(executorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS));
        System.out.println("Thread pool shut down successfully.");
    }

    // Java 线程同步练习题 (synchronized, Lock)
    @Test
    void testThreadSynchronization() throws InterruptedException {
        // 使用 synchronized 关键字
        class SynchronizedCounter {
            private int count = 0;

            public synchronized void increment() {
                count++;
            }

            public int getCount() {
                return count;
            }
        }

        SynchronizedCounter syncCounter = new SynchronizedCounter();
        Runnable syncTask = () -> {
            for (int i = 0; i < 1000; i++) {
                syncCounter.increment();
            }
        };

        Thread threadA = new Thread(syncTask);
        Thread threadB = new Thread(syncTask);

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
        System.out.println("Synchronized Counter final count: " + syncCounter.getCount());
        assertEquals(2000, syncCounter.getCount());

        // 使用 ReentrantLock
        class LockedCounter {
            private int count = 0;
            private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

            public void increment() {
                lock.lock();
                try {
                    count++;
                } finally {
                    lock.unlock();
                }
            }

            public int getCount() {
                return count;
            }
        }

        LockedCounter lockedCounter = new LockedCounter();
        Runnable lockTask = () -> {
            for (int i = 0; i < 1000; i++) {
                lockedCounter.increment();
            }
        };

        Thread threadC = new Thread(lockTask);
        Thread threadD = new Thread(lockTask);

        threadC.start();
        threadD.start();

        threadC.join();
        threadD.join();
        System.out.println("Locked Counter final count: " + lockedCounter.getCount());
        assertEquals(2000, lockedCounter.getCount());
    }

    // Java 并发工具类练习题 (CountDownLatch, Semaphore)
    @Test
    void testConcurrencyUtils() throws InterruptedException {
        // CountDownLatch 练习
        int numberOfTasks = 3;
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(numberOfTasks);
        System.out.println("CountDownLatch created with count: " + numberOfTasks);

        for (int i = 0; i < numberOfTasks; i++) {
            final int taskId = i;
            new Thread(() -> {
                try {
                    System.out.println("Task " + taskId + " started.");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("Task " + taskId + " completed.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown(); // 任务完成，计数器减1
                }
            }).start();
        }

        System.out.println("Main thread waiting for tasks to complete...");
        latch.await(); // 主线程等待所有任务完成
        System.out.println("All tasks completed. Main thread continues.");

        // Semaphore 练习
        int permits = 2; // 允许2个线程同时访问
        java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(permits);
        System.out.println("\nSemaphore created with " + permits + " permits.");

        for (int i = 0; i < 5; i++) {
            final int workerId = i;
            new Thread(() -> {
                try {
                    System.out.println("Worker " + workerId + " trying to acquire permit.");
                    semaphore.acquire(); // 获取许可
                    System.out.println("Worker " + workerId + " acquired permit, working...");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("Worker " + workerId + " finished work, releasing permit.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release(); // 释放许可
                }
            }).start();
        }
        Thread.sleep(3000); // 简单等待所有 worker 完成
        System.out.println("Semaphore demonstration finished.");
    }

    // Java CompletableFuture 练习题
    @Test
    void testCompletableFuture() throws java.util.concurrent.ExecutionException, InterruptedException {
        System.out.println("Starting CompletableFuture exercises.");

        // 练习1: 异步执行一个任务
        java.util.concurrent.CompletableFuture<String> future1 = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running async task 1.");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Result from Async Task 1";
        });

        System.out.println("Submitted Async Task 1, waiting for result...");
        assertEquals("Result from Async Task 1", future1.get());
        System.out.println("Async Task 1 completed.");

        // 练习2: 任务完成后执行回调 (thenApply)
        java.util.concurrent.CompletableFuture<String> future2 = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running async task 2.");
            return "Hello";
        }).thenApply(result -> {
            System.out.println(Thread.currentThread().getName() + " is processing result: " + result);
            return result + " World!";
        });

        assertEquals("Hello World!", future2.get());
        System.out.println("Async Task 2 with thenApply completed.");

        // 练习3: 任务完成后执行另一个异步任务 (thenCompose)
        java.util.concurrent.CompletableFuture<String> future3 = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running async task 3 (part 1).");
            return "Step 1";
        }).thenCompose(result -> java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running async task 3 (part 2) with result: " + result);
            return result + " -> Step 2";
        }));

        assertEquals("Step 1 -> Step 2", future3.get());
        System.out.println("Async Task 3 with thenCompose completed.");

        // 练习4: 组合两个独立的 CompletableFuture (thenCombine)
        java.util.concurrent.CompletableFuture<String> greetingFuture = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is getting greeting.");
            return "Greetings";
        });

        java.util.concurrent.CompletableFuture<String> nameFuture = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is getting name.");
            return "Gemini";
        });

        java.util.concurrent.CompletableFuture<String> combinedFuture = greetingFuture.thenCombine(nameFuture, (greeting, name) -> {
            System.out.println(Thread.currentThread().getName() + " is combining results.");
            return greeting + ", " + name + "!";
        });

        assertEquals("Greetings, Gemini!", combinedFuture.get());
        System.out.println("Combined CompletableFuture with thenCombine completed.");

        // 练习5: 异常处理 (exceptionally)
        java.util.concurrent.CompletableFuture<String> errorFuture = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running error task.");
            if (true) {
                throw new RuntimeException("Something went wrong!");
            }
            return "This will not be returned.";
        }).exceptionally(ex -> {
            System.err.println(Thread.currentThread().getName() + " caught exception: " + ex.getMessage());
            return "Fallback value due to error.";
        });

        assertEquals("Fallback value due to error.", errorFuture.get());
        System.out.println("CompletableFuture with exception handling completed.");

        // 练习6: 所有任务都完成后执行 (allOf)
        java.util.concurrent.CompletableFuture<Void> allOfFuture = java.util.concurrent.CompletableFuture.allOf(
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println("AllOf Task 1 completed.");
            }),
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try { Thread.sleep(400); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println("AllOf Task 2 completed.");
            })
        );
        System.out.println("Waiting for allOf tasks to complete...");
        allOfFuture.get(); // 等待所有 future 完成
        System.out.println("All allOf tasks completed.");
    }
}
