package com.example.demo.service.impl;

import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.primary.UserInfoMapper;
import com.example.demo.service.UserInfoService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    /**
     * caffeine是不缓存null值的，
     * 如果在load的时候返回null，caffeine将会把对应的key从缓存中删除，
     * 同时，loadAll返回的map里是不可以包含value为null的数据，否则将会报NullPointerException
     * 返回 null 将导致 Caffeine 认为该值不需要缓存，下次查询还会继续调用 load 方法，缓存并没生效，导致查询数据库，可能造成缓存击穿
     * <p>
     * refreshAfterWrite就是设置写入后多就会刷新，expireAfterWrite和refreshAfterWrite
     * 的区别是，当缓存过期后，配置了expireAfterWrite，则调用时会阻塞，等待缓存计算完成，返
     * 回新的值并进行缓存，refreshAfterWrite则是返回一个旧值，并异步计算新值并
     * <p>
     * <p>
     * <p>
     * refreshAfterWrite < expireAfterWrite,数据在过期之前都是最新的
     */
    @Autowired
    Cache<String, Object> caffeineCache;

    private Cache<Integer, UserInfo> caffeineCacheAutoLoad = Caffeine.newBuilder()
            // 设置最后一次写入或访问后经过固定时间过期,惰性删除
            .expireAfterWrite(20, TimeUnit.SECONDS)
            .refreshAfterWrite(3, TimeUnit.SECONDS)
            // 初始的缓存空间大小
            .initialCapacity(100)
            // 缓存的最大条数
            .maximumSize(1000)
            //removalListener：当缓存被移除的时候执行的策略，例如打日志等
            .removalListener((key, value, removalCause) -> {
                log.info("key = {}, value = {}, removalCause = {}", key, value, removalCause);
            })
            /**
             * build参数CacheLoader：用于refresh时load缓存的策略，根据具体业务而定，
             * 建议在实现load方法的同时实现loadAll方法loadAll方法适用于批量查缓存的需求，
             * 或者刷新缓存涉及到网络交互等耗时操作。比如你的缓存数据需要从redis里获取，
             * 如果不实现loadAll，则需要多次load操作，也就需要多次redis交互，非常耗时，
             * 而实现loadAll，则可以在loadAll里向redis发送一条批量请求，显著降低网络交互次数和时间，显著提升效率。
             *
             * LoadingCache是惰性刷新
             */
            .build(new CacheLoader<>() {
                       @Override
                       public @Nullable UserInfo load(@NonNull Integer key) {
                           // 如果缓存中不存在，则从库中查找
                           log.info("get from db ， load");
                           return userInfoMapper.selectByPrimaryKey(key);
                       }
                   }
            );

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void addUserInfo(UserInfo userInfo) {
        log.info("create");
        userInfoMapper.insert(userInfo);
        // 加入缓存
        caffeineCache.put(String.valueOf(userInfo.getId()), userInfo);
    }


    @Override
    public UserInfo getById(Integer id) {
        // 先从缓存读取
        caffeineCache.getIfPresent(id);
        UserInfo userInfo = (UserInfo) caffeineCache.asMap().get(String.valueOf(id));
        if (userInfo != null) {
            return userInfo;
        }
        // 如果缓存中不存在，则从库中查找
        log.info("get from db");
        userInfo = userInfoMapper.selectByPrimaryKey(id);
        // 如果用户信息不为空，则加入缓存
        if (userInfo != null) {
            caffeineCache.put(String.valueOf(userInfo.getId()), userInfo);
        }
        return userInfo;
    }


    @Override
    public UserInfo getByIdAutoLoad(Integer id) {
        // 先从缓存读取，再set
        UserInfo userInfo = caffeineCacheAutoLoad.get(id, e -> userInfoMapper.selectByPrimaryKey(id));
        System.out.println(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        log.info("update");
        // 取旧的值
        UserInfo oldUserInfo = userInfoMapper.selectByPrimaryKey(userInfo.getId());
        if (oldUserInfo == null) {
            return null;
        }

        // 将新的对象存储，更新旧对象信息
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
        // 替换缓存中的值
        caffeineCache.put(String.valueOf(oldUserInfo.getId()), oldUserInfo);
        return oldUserInfo;
    }

    @Override
    public void deleteById(Integer id) {
        log.info("delete");
        userInfoMapper.deleteByPrimaryKey(id);
        // 从缓存中删除
        caffeineCache.asMap().remove(String.valueOf(id));
    }
}
