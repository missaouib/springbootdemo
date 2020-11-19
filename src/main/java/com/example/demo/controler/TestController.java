package com.example.demo.controler;

import com.example.demo.entity.User;
import com.example.demo.kafka.KafkaSender;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;

@RestController
@Slf4j
public class TestController {
    @Autowired
    UserService userService;

    @Autowired
    private KafkaSender kafkaSender;

    @GetMapping("/test")
    public String test() {
        return "OK";
    }


    @GetMapping("/kafka")
    public void kafka() {
        for (int i = 0; i < 100000; i++) {
            kafkaSender.send();
        }
    }
}
