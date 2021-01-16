package com.example.demo;

import com.example.demo.entity.Blog;
import com.example.demo.entity.BlogExample;
import com.example.demo.entity.User;
//import com.example.demo.kafka.KafkaReceiver;
import com.example.demo.kafka.KafkaReceiver;
import com.example.demo.kafka.KafkaSender;
import com.example.demo.mapper.secondary.BlogMapper;
import com.example.demo.mapstruct.Person;
import com.example.demo.mapstruct.PersonDTO;
import com.example.demo.mapstruct.PersonMapper;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private KafkaSender kafkaSender;

    @Autowired
    private KafkaReceiver kafkaReceiver;

    @Test
    void contextLoads() {
    }

    @Test
    public void test1() {
        redissonClient.getBucket("aa").set(4);
        System.out.println(redissonClient.getKeys().remainTimeToLive("aa"));
    }

    @Test
    public void test11() {
        BlogExample example = new BlogExample();
        BlogExample.Criteria criteria = example.createCriteria();
        criteria.andAuthorEqualTo("aa");
        int delete = blogMapper.deleteByExample(example);
        System.err.println(delete);
    }

    @Test
    public void test2() {
        RLock disLock = redissonClient.getLock("DISLOCK");
        boolean isLock;
        try {
            //尝试获取分布式锁
            isLock = disLock.tryLock(500, 5000, TimeUnit.MILLISECONDS);
            if (isLock) {
                //TODO if get lock success, do something;
                log.info("GET LOCK");
                Thread.sleep(1000);
            }
        } catch (Exception e) {
        } finally {
            // 无论如何, 最后都要解锁
            disLock.unlock();
        }
    }


    @Test
    public void test3(){
        RMap map = redissonClient.getMap("mymap");
        map.put("a","b");
        System.out.println(map.get("a"));
        map.put("c","d");
        System.out.println(map.get("a"));
        RKeys keys = redissonClient.getKeys();
        System.out.println(keys.toString());
        Iterable<String> allKeys = keys.getKeys();
        allKeys.forEach(System.out::println);
        String randomKey = keys.randomKey();
        System.out.println(randomKey);
        long keysAmount = keys.count();
        System.out.println(keysAmount);
        System.out.println("----------");
        RHyperLogLog<Integer> log = redissonClient.getHyperLogLog("log");
        log.add(1);
        log.add(2);
        log.add(3);
        log.add(4);
        log.count();
    }

    @Test
    public void test4(){
        userService.multiThread();
    }

    @Test
    public void personDTOToPerson() {
        PersonMapper personMapper = PersonMapper.INSTANCE;
        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("zhang");
        personDTO.setLastName("ke");
        Person person = personMapper.personDTOToPerson(personDTO);
        log.error(person.toString());
        log.error(personDTO.toString());
        assertEquals(person.getLastName(),personDTO.getLastName());
        assertEquals(person.getName(),personDTO.getFirstName());
    }

}
