package com.example.demo.controller;

import com.example.commonapi.HelloService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    private volatile boolean flag = true;

    @Autowired
    private UserService userService;

    @DubboReference
    private HelloService helloService;

    @GetMapping("/1")
    public String test() {
        Object[] objects = {"1", "2", "3","4"};
        log.error("a: {},  b: {} , c:{}",objects);
        return "OK";
    }

    @GetMapping("/hello")
    public String hello(){
        String res = helloService.sayHello("test");
        System.out.println(res);
        return res;
    }

    @GetMapping("/log")
    public String testLog() {
        String str = "# 编码知识点\n";
        while (flag){
            log.info("str : {}",str);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "OK";
    }

    @GetMapping("/false")
    public String setFalse(){
        flag = false;
        return flag ? "true" : "false";
    }

    @GetMapping("/true")
    public String setTrue(){
        flag = true;
        return flag ? "true" : "false";
    }
}
