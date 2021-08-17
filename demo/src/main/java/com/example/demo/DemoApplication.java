package com.example.demo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.rocketmq.client.log.ClientLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableCaching
@EnableDubbo
@SpringBootApplication
//@EnableAsync
//@EnableScheduling
public class DemoApplication {

    public static void main(String[] args) {
        System.setProperty(ClientLogger.CLIENT_LOG_LEVEL, "WARN");
//        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
        System.setProperty(ClientLogger.CLIENT_LOG_ROOT, "demo/log");
        SpringApplication.run(DemoApplication.class, args);
    }
}
