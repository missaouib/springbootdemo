package com.example.demo.config;

import com.example.demo.rocketmq.ProducerHook;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MQProducerConfiguration {
    @Value("${rocketmq.producer.groupName}")
    private String groupName;

    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize;

    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;

    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;

    /**
     * @ConditionalOnMissingBean作用：判断当前需要注入Spring容器中的bean的实现类是否已经含有，有的话不注入，没有就注入
     * @ConditionalOnBean作用：判断当前需要注册的bean的实现类否被spring管理，如果被管理则注入，反之不注入
     * @return
     * @throws RuntimeException
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultMQProducer defaultMQProducer() throws RuntimeException {
        DefaultMQProducer producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.namesrvAddr);
        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
        //如果需要同一个 jvm 中不同的 producer 往不同的 mq 集群发送消息，需要设置不同的 instanceName
        //producer.setInstanceName(instanceName);
        //如果发送消息的最大限制
        producer.setMaxMessageSize(this.maxMessageSize);
        //如果发送消息超时时间
        producer.setSendMsgTimeout(this.sendMsgTimeout);
        //如果发送消息失败，设置重试次数，默认为 2 次
        producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        producer.getDefaultMQProducerImpl().registerSendMessageHook(new ProducerHook());
        try {
            producer.start();
            log.info("producer is started. groupName:{}, namesrvAddr: {}", groupName, namesrvAddr);
        } catch (MQClientException e) {
            log.error("failed to start producer.", e);
            throw new RuntimeException(e);
        }
        return producer;
    }
}
