package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class Log4j2Test {

    @Test
    public void test1() {
        //通过打印i，日志文件中数字越小代表越老
        log.info("是否为异步日志：{}", AsyncLoggerContextSelector.isSelected());
        for (int i = 0; i < 50000; i++) {
            log.info("{}", i);
            log.warn("{}", i);
            log.debug("{}", i);
            log.error("{}", i);
        }
    }
}
