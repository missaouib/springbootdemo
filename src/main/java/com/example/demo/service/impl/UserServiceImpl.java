package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.entity.UserExample;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
        UserExample example = new UserExample();
//        UserExample.Criteria criteria = example.createCriteria();
        List<User> users = userDao.selectAll();
        return users;
    }

    @Override
    public Integer save(User user) {
        return userDao.save(user);
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public void MultiThread() {
        threadPoolTaskExecutor.execute(() -> {
            User user = userDao.findById(1);
            System.out.println(user);
            log.info(user.toString());
        });
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
