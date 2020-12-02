package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public List<User> getList() {
        List<User> users = userDao.selectAll();
        return users;
    }

    @Override
    public int insert(User user) {
        return userDao.insert(user);
    }

    @Override
    public int batchInsert(List<User> list) {
        return userDao.batchInsert(list);
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public void multiThread() {
        threadPoolTaskExecutor.execute(() -> {
            User user = userDao.findById(1);
            System.out.println(user);
            log.info(user.toString());
        });
    }


    @Async("taskExecutorA")
    @Override
    public void multiThreadId(int id) {
        User user = userDao.findById(id);
        log.error(user.toString());
        throw new ArithmeticException("异步 无返回值 抛出异常");
    }

    @Override
    @Async("taskExecutorA")
    public Future<User> multiThreadFutureId(int id) {
        User user = userDao.findById(id);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(user);
    }

    @Override
    @Async("taskExecutorA")
    public Future<String> doTask1() throws Exception {
        log.error("开始做任务1");
        long start = System.currentTimeMillis();
        Random random = new Random();
        Thread.sleep(random.nextInt(3000));
        long end = System.currentTimeMillis();
        log.error("完成任务1，耗时：" + (end - start) + "毫秒");
        return new AsyncResult<>("任务1完成");
    }

    @Override
    @Async("taskExecutorA")
    public Future<String> doTask2() throws Exception {
        log.error("开始做任务2");
        long start = System.currentTimeMillis();
        Random random = new Random();
        Thread.sleep(random.nextInt(3000));
        long end = System.currentTimeMillis();
        log.error("完成任务2，耗时：" + (end - start) + "毫秒");
        return new AsyncResult<>("任务2完成");
    }

    @Override
    @Async("taskExecutorA")
    public Future<String> doTask3() throws Exception {
        log.error("开始做任务3");
        long start = System.currentTimeMillis();
        Random random = new Random();
        Thread.sleep(random.nextInt(3000));
        long end = System.currentTimeMillis();
        log.error("完成任务3，耗时：" + (end - start) + "毫秒");
        return new AsyncResult<>("任务3完成");
    }


    public void redisLock() {
        RLock rLock = redissonClient.getLock("myLock");
        rLock.lock(10, TimeUnit.SECONDS);
        RLock disLock = redissonClient.getLock("DISLOCK");
        boolean isLock;
        try {
            //尝试获取分布式锁
            isLock = disLock.tryLock(500, 15000, TimeUnit.MILLISECONDS);
            if (isLock) {
                //TODO if get lock success, do something;
                Thread.sleep(15000);
            }
        } catch (Exception e) {
        } finally {
            // 无论如何, 最后都要解锁
            disLock.unlock();
        }
    }
}
