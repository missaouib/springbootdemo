package com.example.demo.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MessageListenerHandler implements MessageListenerConcurrently {
    private static String TOPIC = "DemoTopic";

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                    ConsumeConcurrentlyContext context) {
        /**     注意： 消费者端的 msgId 对应与生产者端的 offsetMsgId
         *      消费者端的 UNIQ_KEY 对应与生产者端的 msgId
         * msgId：该 ID 是消息发送者在消息发送时会首先在客户端生成，全局唯一，在 RocketMQ 中该 ID 还有另外的一个叫法：uniqId，无不体现其全局唯一性。
         * offsetMsgId：消息偏移ID，该 ID 记录了消息所在集群的物理地址，主要包含所存储 Broker 服务器的地址( IP 与端口号)以及所在commitlog 文件的物理偏移量。
         *
         * 温馨提示：如果消息消费失败需要重试，RocketMQ 的做法是将消息重新发送到 Broker 服务器，此时全局 msgId 是不会发送变化的，
         * 但该消息的 offsetMsgId 会发送变化，因为其存储在服务器中的位置发生了变化。
         */
        for (MessageExt msg : msgs) {

            System.out.println("body:-------->" + new String(msg.getBody()));
            System.out.println("toString:-------->" + msg.toString());
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    private void mockConsume(String msg) {
        log.info("receive msg: {}.", msg);
    }
}
