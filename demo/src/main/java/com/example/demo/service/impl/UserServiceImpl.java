package com.example.demo.service.impl;

import com.example.demo.cache.RedisService;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService redisService;
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
    public List<User> findAllByPageList1(int pageNum, int pageSize) {
        return userDao.findAllByPageList1(pageNum,pageSize);
    }

    @Override
    public List<User> findAllByPageList2(int pageNum, int pageSize) {
        return userDao.findAllByPageList2(pageNum,pageSize);

    }

    @Override
    public PageInfo<User> findAllByPagePageInfo1(int pageNum, int pageSize) {
        return userDao.findAllByPagePageInfo1(pageNum,pageSize);
    }

    @Override
    public PageInfo<User> findAllByPagePageInfo2(int pageNum, int pageSize) {
        return userDao.findAllByPagePageInfo2(pageNum,pageSize);
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
        throw new ArithmeticException("?????? ???????????? ????????????");
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
        log.error("???????????????1");
        long start = System.currentTimeMillis();
        Random random = new Random();
        Thread.sleep(random.nextInt(3000));
        long end = System.currentTimeMillis();
        log.error("????????????1????????????" + (end - start) + "??????");
        return new AsyncResult<>("??????1??????");
    }

    @Override
    @Async("taskExecutorA")
    public Future<String> doTask2() throws Exception {
        log.error("???????????????2");
        long start = System.currentTimeMillis();
        Random random = new Random();
        Thread.sleep(random.nextInt(3000));
        long end = System.currentTimeMillis();
        log.error("????????????2????????????" + (end - start) + "??????");
        return new AsyncResult<>("??????2??????");
    }

    @Override
    @Async("taskExecutorA")
    public Future<String> doTask3() throws Exception {
        log.error("???????????????3");
        long start = System.currentTimeMillis();
        Random random = new Random();
        Thread.sleep(random.nextInt(3000));
        long end = System.currentTimeMillis();
        log.error("????????????3????????????" + (end - start) + "??????");
        return new AsyncResult<>("??????3??????");
    }
}
