package com.example.demo.controler;

import com.example.demo.entity.User;
import com.example.demo.kafka.KafkaSender;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Slf4j
public class TestController {
    @Autowired
    UserService userService;

    @Autowired
    private KafkaSender kafkaSender;

    @GetMapping("/test")
    public String test(){
        return "OK";
    }

    @GetMapping("random")
    public void random(){
        int size = 100;
        for(int i=0;i<size;i++){
            Random r = new Random();
            Integer randNum = Math.abs(r.nextInt()%100);
            log.info("记录id：{},随机数：{}",i,randNum);

            User user = new User();
//            user.setId(i);
            user.setPassword("000"+i+randNum);
            user.setUsername("wang"+i+randNum);
            userService.save(user);
        }
    }

    @GetMapping("/kafka")
    public void kafka(){
        for (int i = 0; i < 100000; i++) {
            kafkaSender.send();
        }
    }
}
