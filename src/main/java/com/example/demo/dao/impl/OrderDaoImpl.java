package com.example.demo.dao.impl;

import com.example.demo.dao.OrderDao;
import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order findById(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(Order entity) {
        return orderMapper.insert(entity);
    }

    @Override
    public int update(Order entity) {
        return orderMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int delete(Long id) {
        return orderMapper.deleteByPrimaryKey(id);
    }
}
