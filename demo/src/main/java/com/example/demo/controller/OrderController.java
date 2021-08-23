package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/create_order/{code}")
    public void createOrder(@PathVariable String code) throws MQClientException, UnsupportedEncodingException {
        Order order = new Order();
        order.setCount(111);
        order.setAmount(123);
        order.setUserId(1L);
        order.setCommodityCode(code);
        log.info("接收到订单数据：{}", order.getCommodityCode());
        orderService.createOrder(order);
    }
}
