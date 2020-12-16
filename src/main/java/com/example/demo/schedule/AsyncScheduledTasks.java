package com.example.demo.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
/**
 *  定时任务并行执行
 */
public class AsyncScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    /**
     * fixedDelay：固定延迟执行。距离上一次调用成功后2秒才执。
     */
    @Async("taskExecutorB")
    @Scheduled(fixedDelay = 2000)
    public void reportCurrentTimeWithFixedDelay() {
        try {
            TimeUnit.SECONDS.sleep(3);
            log.info("Fixed Delay Task : The time is now {}  Current Thread : {}", dateFormat.format(new Date()),Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
