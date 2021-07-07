package com.example.demo.dao;

import com.example.demo.entity.Points;

public interface PointDao {

    Points findById(Long id);

    int insert(Points entity);

    int update(Points entity);

    int delete(Long id);
}
