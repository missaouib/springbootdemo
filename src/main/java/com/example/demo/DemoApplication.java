package com.example.demo;

import org.apache.rocketmq.client.log.ClientLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableCaching
@SpringBootApplication
//@EnableAsync
//@EnableScheduling
public class DemoApplication {

    public static void main(String[] args) {
        System.setProperty(ClientLogger.CLIENT_LOG_LEVEL,"WARN");
        SpringApplication.run(DemoApplication.class, args);
    }
}
