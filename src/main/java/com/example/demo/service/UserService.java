package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> getList();

    Integer  save(User user);

    User findById(int id);

    void MultiThread();
}
