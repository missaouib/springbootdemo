package com.example.demo.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.TransactionLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderTransactionListener  implements TransactionListener {

    @Autowired
    OrderService orderService;

    @Autowired
    TransactionLogService transactionLogService;
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("开始执行本地事务....");
        LocalTransactionState state;
        try{
            String body = new String(msg.getBody());
            Order order = JSONObject.parseObject(body, Order.class);
            orderService.createOrder(order,msg.getTransactionId());
            state = LocalTransactionState.COMMIT_MESSAGE;
            log.info("本地事务已提交。{}",msg.getTransactionId());
        }catch (Exception e){
            log.info("执行本地事务失败。{}",e);
            state = LocalTransactionState.ROLLBACK_MESSAGE;
        }
//        try {
//            Thread.sleep(100_000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return state;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.info("开始回查本地事务状态。{}",msg.getTransactionId());
        LocalTransactionState state;
        String transactionId = msg.getTransactionId();
        if (transactionLogService.get(transactionId)>0){
            state = LocalTransactionState.COMMIT_MESSAGE;
        }else {
            state = LocalTransactionState.UNKNOW;
        }
        log.info("结束本地事务状态查询：{}",state);
        return state;
    }
}
