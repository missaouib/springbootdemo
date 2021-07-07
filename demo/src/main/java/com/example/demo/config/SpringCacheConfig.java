package com.example.demo.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SpringCacheConfig {

    @Autowired
    RedissonClient redissonClient;

    @Bean
    CacheManager cacheManager() {
        Map<String, CacheConfig> config = new HashMap<>();
        config.put("bookCache", new CacheConfig(60*1000, 60*1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
