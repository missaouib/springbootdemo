package com.example.demo.service.impl;

import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.primary.UserInfoMapper;
import com.example.demo.service.UserInfoService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    Cache<String, Object> caffeineCache;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void addUserInfo(UserInfo userInfo) {
        log.info("create");
        userInfoMapper.insert(userInfo);
        // 加入缓存
        caffeineCache.put(String.valueOf(userInfo.getId()),userInfo);
    }

    @Override
    public UserInfo getById(Integer id) {
        // 先从缓存读取
        caffeineCache.getIfPresent(id);
        UserInfo userInfo = (UserInfo) caffeineCache.asMap().get(String.valueOf(id));
        if (userInfo != null){
            return userInfo;
        }
        // 如果缓存中不存在，则从库中查找
        log.info("get from db");
        userInfo = userInfoMapper.selectByPrimaryKey(id);
        // 如果用户信息不为空，则加入缓存
        if (userInfo != null){
            caffeineCache.put(String.valueOf(userInfo.getId()),userInfo);
        }
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
        caffeineCache.put(String.valueOf(oldUserInfo.getId()),oldUserInfo);
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
