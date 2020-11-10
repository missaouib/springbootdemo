package com.example.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() throws IOException {
//        Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-cluster.yaml"));
        Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-single.yaml"));
        return Redisson.create(config);
    }

}