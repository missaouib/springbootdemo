package com.example.demo.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;

/**
 * RocKetMQ 中提供了两个 hook 接口：SendMessageHook 和 ConsumeMessageHook 接口，
 * 可以用于在消息发送之前、之后，消息消费之前、之后对消息进行拦截
 *
 * // 注册 SendMessageHook
 * producer.getDefaultMQProducerImpl().registerSendMessageHook(new ProducerTestHook());
 */
@Slf4j
public class ProducerHook implements SendMessageHook {
    @Override
    public String hookName() {
        return ProducerHook.class.getName();
    }

    @Override
    public void sendMessageBefore(SendMessageContext sendMessageContext) {
        log.info("execute sendMessageBefore. sendMessageContext:{}", sendMessageContext);
    }

    @Override
    public void sendMessageAfter(SendMessageContext sendMessageContext) {
        log.info("execute sendMessageAfter. sendMessageContext:{}", sendMessageContext);
    }
}
