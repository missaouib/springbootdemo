package com.example.demo;

import com.example.demo.cache.RedisService;
import com.example.demo.entity.Book;
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
}
