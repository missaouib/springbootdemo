package com.example.demo;

import com.example.demo.commonspool2.NeedPooledObject;
import com.example.demo.commonspool2.NeedPooledObjectPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@SpringBootTest
public class CommonPool2Test {

    @Autowired
    private NeedPooledObjectPool needPooledObjectPool;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.execute(() -> {
                NeedPooledObject needPooledObject = null;
                try {
                    needPooledObject = needPooledObjectPool.borrowObject();
                    log.info("{}",needPooledObject);
                } catch (Exception e) {
                    log.error("{}",e);
                } finally {
                    if (needPooledObject != null) {
                        //最终归还对象到对象池
                        needPooledObjectPool.returnObject(needPooledObject);
                    }
                }
            });
        }
        System.out.println("done!");
    }
}
