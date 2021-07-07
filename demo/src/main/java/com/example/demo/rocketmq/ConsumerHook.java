package com.example.demo.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;

@Slf4j
public class ConsumerHook implements ConsumeMessageHook {
    @Override
    public String hookName() {
        return ConsumerHook.class.getName();
    }

    @Override
    public void consumeMessageBefore(ConsumeMessageContext consumeMessageContext) {
        log.info("execute consumeMessageBefore. consumeMessageContext: {}", consumeMessageContext);
    }

    @Override
    public void consumeMessageAfter(ConsumeMessageContext consumeMessageContext) {
        log.info("execute consumeMessageAfter. consumeMessageContext: {}", consumeMessageContext);
    }
}
