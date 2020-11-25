package com.example.demo.controler;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RocketMQControler {
    private static String TOPIC = "DemoTopic";
    private static String TAGS = "glmapperTags";

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @RequestMapping("send")
    public String test() throws Throwable {
        Message msg = new Message(TOPIC, TAGS, ("Say Hello RocketMQ to Glmapper").getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 调用客户端发送消息
        SendResult sendResult = defaultMQProducer.send(msg);
        log.info("sendResult: {}.",sendResult);
        return "SUCCESS";
    }
}
