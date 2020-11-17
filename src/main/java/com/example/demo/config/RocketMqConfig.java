package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
//@Configuration
public class RocketMqConfig {

    @Value("${rocketmq.nameserver.addr}")
    private String nameServerAddr;

    @Value("${rocketmq.producer.group}")
    private String producerGroupName;
    @Value("${rocketmq.producer.retry-times}")
    private Integer retryTimes;
    @Value("${rocketmq.producer.timeout}")
    private Integer timeout;


    @Value("${rocketmq.consumer.group}")
    private String consumerGroupName;

    @Autowired
    private MessageListenerConcurrently messageListener;


    @Bean(destroyMethod = "shutdown")
    public DefaultMQProducer defaultMQProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroupName);
        producer.setNamesrvAddr(nameServerAddr);
        producer.setRetryTimesWhenSendFailed(retryTimes);
        producer.setSendMsgTimeout(timeout);
        try {
            producer.start();
            log.info("rocketMQ producer start success");
        } catch (MQClientException e) {
            log.error("rocketMQ producer start error", e);
        }
        return producer;
    }


    @Bean(destroyMethod = "shutdown")
    public DefaultMQPushConsumer defaultMQPushConsumer() {

//        for (String topic : topics) {
//            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(producerGroupName + topic);
//            consumer.setNamesrvAddr(nameServerAddr);
//            consumer.registerMessageListener(messageListener);
//            try {
//                consumer.subscribe(topic, "*");
//                consumer.start();
//                log.info("rocketMQ topic={} consumer start success", topic);
//            } catch (MQClientException e) {
//                log.error("rocketMQ topic=" + topic + " consumer start error", e);
//            }
//        }
        return null;
    }

}