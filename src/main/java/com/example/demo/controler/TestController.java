package com.example.demo.controler;

import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {
    @Autowired
    UserService userService;

    @GetMapping("/1")
    public String test() {
        Object[] objects = {"1", "2", "3","4"};
        log.error("a: {},  b: {} , c:{}",objects);
        return "OK";
    }
}
