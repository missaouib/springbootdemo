package com.example.demo.commonspool2;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.time.Duration;

/**
 * 对象池自动装配
 *
 */
@Configuration
@EnableConfigurationProperties(PoolProperties.class)
public class PoolAutoConfiguration {
    private final PoolProperties poolProperties;
    private NeedPooledObjectPool pool;

    @Autowired
    public PoolAutoConfiguration(PoolProperties poolProperties) {
        this.poolProperties = poolProperties;
    }

    @ConditionalOnClass({NeedPooledObjectFactory.class})
    @Bean
    protected NeedPooledObjectPool getNeedPooledObjectFactory() {
        NeedPooledObjectFactory needPooledObjectFactory = new NeedPooledObjectFactory();
        //设置对象池的相关参数
        GenericObjectPoolConfig<NeedPooledObject> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(poolProperties.getMaxIdle());
        poolConfig.setMaxTotal(poolProperties.getMaxTotal());
        poolConfig.setMinIdle(poolProperties.getMinIdle());
        poolConfig.setMaxWait(Duration.ofSeconds(poolProperties.getMaxWait()));
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(1000 * 60 * 30));
        //一定要关闭jmx，不然springboot启动会报已经注册了某个jmx的错误
        poolConfig.setJmxEnabled(false);

        //新建一个对象池,传入对象工厂和配置
        pool = new NeedPooledObjectPool(needPooledObjectFactory, poolConfig);

        initPool(poolProperties.getInitialSize(), poolProperties.getMaxIdle());
        return pool;
    }

    /**
     * 预先加载testObject对象到对象池中
     *
     * @param initialSize 初始化连接数
     * @param maxIdle     最大空闲连接数
     */
    private void initPool(int initialSize, int maxIdle) {
        if (initialSize <= 0) {
            return;
        }

        int size = Math.min(initialSize, maxIdle);
        for (int i = 0; i < size; i++) {
            try {
                pool.addObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (pool != null) {
            pool.close();
        }
    }
}
