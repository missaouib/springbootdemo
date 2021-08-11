package com.example.demo.service.impl;

import com.example.demo.dao.OrderDao;
import com.example.demo.dao.TransactionLogDao;
import com.example.demo.entity.Order;
import com.example.demo.entity.TransactionLog;
import com.example.demo.service.OrderService;
import com.example.demo.util.SnowFlake;
import com.example.demo.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    TransactionLogDao transactionLogDao;
    @Autowired(required = false)
    TransactionMQProducer producer;

    SnowFlake snowflake = new SnowFlake(1, 1);

    @Override
    public Order findById(Long id) {
        return orderDao.findById(id);
    }

    @Override
    public int insert(Order entity) {
        return orderDao.insert(entity);
    }

    @Override
    public int update(Order entity) {
        return orderDao.update(entity);
    }

    @Override
    public int delete(Long id) {
        return orderDao.delete(id);
    }

    //执行本地事务时调用，将订单数据和事务日志写入本地数据库
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrder(Order order, String transactionId) {

        //1.创建订单
        orderDao.insert(order);

        //2.写入事务日志
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setId(transactionId);
        transactionLog.setBusiness("order");
        transactionLog.setForeignKey(String.valueOf(order.getId()));
        transactionLogDao.insert(transactionLog);

        log.info("订单创建完成 {}", order);
    }

    //前端调用，只用于向RocketMQ发送事务消息
    @Override
    public void createOrder(Order order) throws MQClientException, UnsupportedEncodingException {
        order.setId(snowflake.nextId());
        order.setOrderNo(snowflake.nextId());
        Message message = new Message();
        message.setBody(JsonUtil.toJsonString(order).getBytes());
        message.setTopic("order");
        producer.sendMessageInTransaction(message, "order");
    }

}
