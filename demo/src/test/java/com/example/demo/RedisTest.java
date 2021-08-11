package com.example.demo;

import com.example.demo.cache.RedisService;
import com.example.demo.entity.Book;
import com.example.demo.util.json.JsonEnum;
import com.example.demo.util.json.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisService redisService;

    @Test
    public void test1(){
        Book book = new Book();
        book.setName("aaa");
        book.setPrice(new BigDecimal("123.45"));
        book.setId(1111);
        book.setCreateTime(new Date());
        redisService.set("a",book);
        System.err.println(redisService.get("a"));

    }

    @Test
    public void test2(){
        JsonUtil.initJson(JsonEnum.GSON);
        Book book = new Book();
        book.setId(123);
        book.setPrice(new BigDecimal("11.34"));
        book.setName("hello");
        book.setCreateTime(new Date());
        String s = JsonUtil.toJsonString(book);
        System.out.println(s);
        redisService.set("s1",s);
        redisService.set("s11",book);

        test3();
    }

    @Test
    public void test3(){
        ObjectMapper objectMapper = new ObjectMapper();
        Book book = new Book();
        book.setId(123);
        book.setPrice(new BigDecimal("11.34"));
        book.setName("hello");
        book.setCreateTime(new Date());
        String s = null;
        try {
            s = objectMapper.writeValueAsString(book);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(s);
        redisService.set("s2",s);
        redisService.set("s22",book);
    }
}
