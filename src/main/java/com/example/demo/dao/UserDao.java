package com.example.demo.dao;

import com.example.demo.entity.User;

import java.util.List;

public interface UserDao {
    List<User> selectAll();

    User findById(int id);

    int insert(User entity);

    int batchInsert(List<User> list);
}
