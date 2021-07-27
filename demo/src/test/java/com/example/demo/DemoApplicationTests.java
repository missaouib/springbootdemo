package com.example.demo;

import com.example.demo.entity.BlogExample;
import com.example.demo.kafka.KafkaReceiver;
import com.example.demo.kafka.KafkaSender;
import com.example.demo.mapper.secondary.BlogMapper;
import com.example.demo.mapstruct.Person;
import com.example.demo.mapstruct.PersonDTO;
import com.example.demo.mapstruct.PersonMapper;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private BlogMapper blogMapper;


    @Autowired
    private KafkaSender kafkaSender;

    @Autowired
    private KafkaReceiver kafkaReceiver;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    public void test0() {


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
    public void test4() {
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
        assertEquals(person.getLastName(), personDTO.getLastName());
        assertEquals(person.getName(), personDTO.getFirstName());
    }

}
