package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootTest
public class JedisTest {
    @Autowired
    private JedisPool jedisPool;

    @Test
    public void test1() {
        Jedis jedis = jedisPool.getResource();
        String res = jedis.set("jedis", "Jedis");
        System.err.println(res);
        System.err.println(jedis.get("jedis"));
    }

}
