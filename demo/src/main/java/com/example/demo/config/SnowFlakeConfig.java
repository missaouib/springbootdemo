package com.example.demo.config;

import com.example.demo.util.SnowFlake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowFlakeConfig {

    @Value("${snowflake.datacenterId}")
    private long datacenterId;

    @Value("${snowflake.machineId}")
    private long machineId;

    @Bean
    public SnowFlake getSnowFlake(){
        return new SnowFlake(datacenterId,machineId);
    }
}
