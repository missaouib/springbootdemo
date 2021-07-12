package com.example.provider.impl;

import com.example.commonapi.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

@DubboService
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public String sayHello(String name) {
        log.info("{}",String.format("[%s] : Hello, %s", serviceName, name));
        return String.format("[%s] : Hello, %s", serviceName, name);
    }
}
