package com.example.demo.cache.impl;

import com.example.demo.cache.RedisDistributeLock;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisDistributeLockImpl implements RedisDistributeLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 加锁脚本
     */
    private DefaultRedisScript<Long> lockScript;

    /**
     * 解锁脚本
     */
    private DefaultRedisScript<Long> unlockScript;

    public RedisDistributeLockImpl() {
        init();
    }

    /**
     * 生成
     */
    private void init() {
        try {
            String LOCK_LUA_SCRIPT = IOUtils.toString(ResourceUtils.getURL("classpath:lua/lock.lua").openStream(), StandardCharsets.UTF_8);
            String UNLOCK_LUA_SCRIPT = IOUtils.toString(ResourceUtils.getURL("classpath:lua/unlock.lua").openStream(), StandardCharsets.UTF_8);
            lockScript = new DefaultRedisScript<>(LOCK_LUA_SCRIPT, Long.class);
            unlockScript = new DefaultRedisScript<>(UNLOCK_LUA_SCRIPT, Long.class);
        } catch (Exception e) {
            log.warn("load lua script exception : ", e);
            throw new RuntimeException("load lua script exception");
        }
    }


    /**
     * 可重入锁
     *
     * @param lockName  锁名字,代表需要争临界资源
     * @param leaseTime 锁释放时间
     * @param unit      锁释放时间单位
     * @return
     */
    public boolean tryLock(String lockName, String request, long leaseTime, TimeUnit unit, int retryTimes) {
        try {
            long internalLockLeaseTime = unit.toMillis(leaseTime);
            Long result = redisTemplate.execute(lockScript, List.of(lockName), internalLockLeaseTime, request);
            log.warn("request : {}, try lock result : {}", request, result);
            if (result != null && result == 1) {
                log.info("success to acquire lock:" + Thread.currentThread().getName() + "request" + request + ", Status code reply:" + result);
                return true;
            } else if (retryTimes == 0) {
                //重试次数为0直接返回失败
                return false;
            } else {
                //重试获取锁
                log.info("retry to acquire lock:" + Thread.currentThread().getName() + "request" + request + ", Status code reply:" + result);
                int count = 0;
                while (true) {
                    try {
                        //休眠一定时间后再获取锁，这里时间可以通过外部设置
                        Thread.sleep(100);
                        result = (Long) redisTemplate.execute(lockScript, Arrays.asList(lockName), internalLockLeaseTime, request);
                        if (result != null && result == 1) {
                            log.info("success to acquire lock:" + Thread.currentThread().getName() + "request" + request + ", Status code reply:" + result);
                            return true;
                        } else {
                            count++;
                            if (retryTimes == count) {
                                log.info("fail to acquire lock for " + Thread.currentThread().getName() + "request" + request + ", Status code reply:" + result);
                                return false;
                            } else {
                                log.warn(count + " times try to acquire lock for " + Thread.currentThread().getName() + "request" + request + ", Status code reply:" + result);
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        log.error("acquire redis occurred an exception:" + Thread.currentThread().getName(), e);
                        break;
                    }

                }
            }
        } catch (Exception e) {
            log.error("acquire redis occurred an exception:" + Thread.currentThread().getName(), e);
        }
        return false;
    }

    @Override
    public boolean lock(String lockName, String request, long leaseTime, TimeUnit unit) {
        try {
            long internalLockLeaseTime = unit.toMillis(leaseTime);
            Long result = redisTemplate.execute(lockScript, List.of(lockName), internalLockLeaseTime, request);
            log.warn("request : {}, lock result : {}", request, result);
            if (result != null && result == 1) {
                log.info("success to acquire lock: " + request + ", result:" + result);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("acquire lock occurred an exception:" + Thread.currentThread().getName(), e);
        }
        return false;
    }


    /**
     * 解锁
     * 若可重入 key 次数大于 1，将可重入 key 次数减 1 <br>
     * 解锁 lua 脚本返回含义：<br>
     * 1:代表解锁成功 <br>
     * 0:代表锁未释放，可重入次数减 1 <br>
     * nil：代表其他线程尝试解锁 <br>
     * <p>
     * 如果使用 DefaultRedisScript<Boolean>，由于 Spring-data-redis eval 类型转化，<br>
     * 当 Redis 返回  Nil bulk, 默认将会转化为 false，将会影响解锁语义，所以下述使用：<br>
     * DefaultRedisScript<Long>
     * <p>
     * 具体转化代码请查看：<br>
     * JedisScriptReturnConverter<br>
     *
     * @param lockName 锁名称
     * @throws IllegalMonitorStateException 解锁之前，请先加锁。若为加锁，解锁将会抛出该错误
     */
    @Override
    public void unlock(String lockName, String request) {
        Long result = redisTemplate.execute(unlockScript, Lists.newArrayList(lockName), request);
        log.warn("request  :{}, unlock result : {}", request, result);
        // 如果未返回值，代表其他线程尝试解锁
        if (result == null) {
            throw new IllegalMonitorStateException("attempt to unlock lock, not locked by lockName: " + lockName + " with request: "
                    + request);
        }
    }
}
