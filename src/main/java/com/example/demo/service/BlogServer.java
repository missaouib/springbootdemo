package com.example.demo.service;

import com.example.demo.entity.Blog;

import java.util.List;

public interface BlogServer {
    List<Blog> selectAll();

    Blog findById(int id);

    List<Blog> findByCondition(Blog entity);

    int insert(Blog entity);

    int update(Blog entity);

    int delete(int id);
}
