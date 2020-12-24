package com.example.demo.config;

import com.example.demo.rocketmq.OrderTransactionListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class MQTransactionProducerConfiguration {
    /**
     * 要求唯一
     */
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

    //用于执行本地事务和事务状态回查的监听器
    @Autowired
    OrderTransactionListener orderTransactionListener;

    //执行任务的线程池
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));


    /**
     * 默认关闭 vip channel :只接收producer的消息，不接受consumer的拉取请求
     * @ConditionalOnMissingBean作用：判断当前需要注入Spring容器中的bean的实现类是否已经含有，有的话不注入，没有就注入
     * @ConditionalOnBean作用：判断当前需要注册的bean的实现类否被spring管理，如果被管理则注入，反之不注入
     * @return
     * @throws RuntimeException
     */
    @Bean
    @ConditionalOnMissingBean
    public TransactionMQProducer TransactionMQProducer() throws RuntimeException {
        TransactionMQProducer producer = new TransactionMQProducer(this.groupName+"Transactional");
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
        producer.setExecutorService(executor);
        producer.setTransactionListener(orderTransactionListener);
//        producer.getDefaultMQProducerImpl().registerSendMessageHook(new ProducerHook());
        try {
            producer.start();
            log.info("Transactional producer is started. groupName:{}, namesrvAddr: {}", groupName, namesrvAddr);
        } catch (MQClientException e) {
            log.error("failed to start transactional producer.", e);
            throw new RuntimeException(e);
        }
        return producer;
    }


}
