package com.example.demo;

import com.example.demo.cache.RedisDistributeLock;
import com.example.demo.cache.RedisService;
import com.example.demo.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class RedisDistributeLockTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisDistributeLock lock;

    static int sum = 0;

    @Autowired
    SnowFlake snowFlake;


    @Test
    public void test1() throws InterruptedException {

        String lockName = "locka";
        CountRunnable countRunnable = new CountRunnable();
        String request = snowFlake.nextIdStr();
        //UUID 同步 自带锁
//            String request = UUID.randomUUID().toString();

        for (int i = 0; i < 100; i++) {

            new Thread(() -> {
                boolean lock1 = lock.tryLock(lockName, request, 30, TimeUnit.SECONDS,3);
                if (lock1){
                    for (int j = 0; j < 100; j++) {
                        sum++;
                    }

                }
                else return;

//                boolean tryLock = lock.tryLock(lockName, request, 3, TimeUnit.SECONDS, 3);
//                log.info("try lock : {}",tryLock);
//                RLock rLock = redissonClient.getLock("ssr");
//                rLock.lock();


                log.info("sum : " + sum);
                lock.unlock(lockName, request);
//                rLock.unlock();
            }).start();
//            threadPoolTaskExecutor.execute(() ->{
////                String request = UUID.randomUUID().toString();
////                lock.lock(lockName,request,3, TimeUnit.SECONDS);
////                redissonClientLock.lock();
//                for (int j = 0; j < 200; j++) {
//                    sum++;
//                }
//                System.out.println(sum);
//
////                System.out.println(sum);
////                log.info(String.valueOf(sum));
////                redissonClientLock.unlock();
////                lock.unlock(lockName,request);
//            });
        }
        Thread.sleep(1000);
        System.out.println(sum);
    }

    @Test
    public void test2() {
        System.out.println(redisTemplate);

        String lockName = "lock3";
        String request = UUID.randomUUID().toString();
        lock.lock(lockName, request, 100, TimeUnit.SECONDS);
        System.out.println(redisService.hGet(lockName, "\"" + request + "\""));
        System.out.println(redisService.hGet(lockName, request));
        System.out.println(redisService.hGetAll(lockName));
//        lock.unlock(lockName,request);

        System.out.println("........................................");
        redisService.hPut("lock5", request, 1);
        System.out.println(redisService.hGetAll("lock5"));
        System.out.println(redisService.hGet("lock5", request));
    }

    @Test
    public void test3() throws InterruptedException {
        RLock rLock = redissonClient.getLock("redissionLock");
        rLock.lock();
        rLock.lock();
        Thread.sleep(10000);

    }

    @Test
    public void test4() {
        String lockName = "lock6";

        String request = snowFlake.nextIdStr();
        lock.lock(lockName, request, 100, TimeUnit.SECONDS);
        System.out.println("hahaha");
        lock.unlock(lockName, request);
    }
}

class CountRunnable implements Runnable {

    private int count = 0;

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            count++;
        }
        System.out.println(count);
    }
}
