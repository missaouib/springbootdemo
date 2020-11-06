package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.entity.UserExample;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getList() {
        UserExample example = new UserExample();
//        UserExample.Criteria criteria = example.createCriteria();
        List<User> users = userMapper.selectByExample(example);
        return users;
    }

    @Override
    public Integer save(User user) {
        return userMapper.insert(user);
    }
}
