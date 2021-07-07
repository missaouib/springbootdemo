package com.example.demo.dao;


import com.example.demo.entity.Order;

public interface OrderDao {
    Order findById(Long id);

    int insert(Order entity);

    int update(Order entity);

    int delete(Long id);
}
