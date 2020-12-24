package com.example.demo.controler;

import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import com.example.demo.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    SnowFlake snowFlake = new SnowFlake(1, 1);

    @GetMapping("/create_order")
    public void createOrder() throws MQClientException, UnsupportedEncodingException {
        Order order = new Order();
        order.setCount(111);
        order.setAmount(123);
        order.setUserId(1L);
        order.setCommodityCode("code");
        log.info("接收到订单数据：{}", order.getCommodityCode());
        orderService.createOrder(order);
    }
}
