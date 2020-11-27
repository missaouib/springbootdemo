package com.example.demo.controler;

import com.example.demo.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mq")
@RestController
@Slf4j
public class MQControler {
    private static String TOPIC = "DemoTopic";
    private static String TAGS = "DemoTags";

    private final DefaultMQProducer defaultMQProducer;

    private final KafkaSender kafkaSender;

    /**
     * 使用基于 constructor 注入，而不是基于 field 注入
     *
     * @param defaultMQProducer
     * @param kafkaSender
     */
    @Autowired
    public MQControler(DefaultMQProducer defaultMQProducer, KafkaSender kafkaSender) {
        this.defaultMQProducer = defaultMQProducer;
        this.kafkaSender = kafkaSender;
    }

    @GetMapping("send")
    public String test() throws Throwable {
        Message msg = new Message(TOPIC, TAGS, ("Hello RocketMQ").getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 调用客户端发送消息
        SendResult sendResult = defaultMQProducer.send(msg);
        log.info("sendResult: {}.", sendResult);
        return "SUCCESS";
    }

    @GetMapping("kafka")
    public void kafka() {
        kafkaSender.send();
    }
}
