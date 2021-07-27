package com.example.provider;

import com.example.commonapi.HelloService;
import com.example.provider.cache.RedisService;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ProviderApplicationTests {

    @Autowired
    private HelloService helloService;

    @Autowired
    private RedisService redisService;


    @Test
    void contextLoads() {
    }

    @Test
    void test1() {
        helloService.sayHello("sss");
        redisService.set("a", "1");
        String a = redisService.get("a").toString();
        System.err.println(a);
        redisService.set("a", "2", 3, TimeUnit.MINUTES);
        String a1 = redisService.get("a").toString();
        System.err.println(a1);
        A a2 = new A();
        a2.setB(2);
        a2.setI(2);
        a2.setStr("ste");
        redisService.set("a2", a2);
        System.err.println(redisService.get("a2"));
        redisService.rename("a", "a1");
        Set<String> set = new HashSet<>();
        redisService.sAdd("setKey","a","b","c");
        redisService.sAdd("setKey1","a","b","d");
        Set<Object> sIntersect = redisService.sIntersect("setKey", "setKey1");
        System.err.println(sIntersect);
    }

}

@Data
class A {
    private Integer i;
    private int b;
    private String str;
}
