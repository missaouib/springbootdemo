package com.example.demo.service;

import com.example.demo.entity.Order;
import org.apache.rocketmq.client.exception.MQClientException;

import java.io.UnsupportedEncodingException;

public interface OrderService {
    Order findById(Long id);

    int insert(Order entity);

    int update(Order entity);

    int delete(Long id);

    void createOrder(Order order, String transactionId);

    void createOrder(Order order) throws MQClientException, UnsupportedEncodingException;
}
