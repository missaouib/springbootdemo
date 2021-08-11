package com.example.demo.controler;

import com.example.demo.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/mq")
@RestController
@Slf4j
public class MqControler {
    private static final String TOPIC = "DemoTopic";
    private static final String TAGS = "DemoTags";

    /**
     * 基于 field 注入
     * 没有启动rocketmq服务端，故不加载 DefaultMQProducer
     */
    @Autowired(required = false)
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private KafkaSender kafkaSender;


    @GetMapping("send")
    public String test() throws Throwable {
        Message msg = new Message(TOPIC, TAGS, ("Hello RocketMQ").getBytes(RemotingHelper.DEFAULT_CHARSET));

        // 调用客户端发送消息 同步
        SendResult sendResult = defaultMQProducer.send(msg);
        log.info("sendResult: {}.", sendResult);
        return "SUCCESS";
    }

    @GetMapping("sendBatch")
    public String sendBatch() throws Throwable {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            messages.add(new Message(TOPIC, TAGS, ("Hello RocketMQ").getBytes(RemotingHelper.DEFAULT_CHARSET)));
        }
        // 调用客户端发送消息 同步
        SendResult sendResult = defaultMQProducer.send(messages);
        log.info("sendResult: {}.", sendResult);
        return "SUCCESS";
    }

    @GetMapping("sendAsync")
    public String sendAsync() throws Throwable {
        Message msg = new Message(TOPIC, TAGS, ("Hello RocketMQ").getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 异步发送消息
        defaultMQProducer.send(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("sendResult: {}.", sendResult);
            }

            @Override
            public void onException(Throwable e) {
                log.error("发送失败 异常：{}", e);
            }
        });
        return "SUCCESS";
    }


    @GetMapping("kafka")
    public void kafka() {
        kafkaSender.send();
    }
}
