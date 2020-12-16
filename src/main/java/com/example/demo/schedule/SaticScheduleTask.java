package com.example.demo.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 静态：基于注解
 * 基于注解@Scheduled默认为单线程，开启多个任务时，任务的执行时机会受上一个任务执行时间的影响。
 */
@Component
@Slf4j
public class SaticScheduleTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * fixedRate：固定速率执行。每5秒执行一次。
     */
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTimeWithFixedRate() {
        log.info("Fixed Rate Task: time is {} Current Thread: {}", dateFormat.format(new Date()), Thread.currentThread().getName());
    }

    /**
     * fixedDelay：固定延迟执行。距离上一次调用成功后2秒才执。
     */
    @Scheduled(fixedDelay = 2000)
    public void reportCurrentTimeWithFixedDelay() {
        try {
            TimeUnit.SECONDS.sleep(3);
            log.info("Fixed Delay Task: time is {} Current Thread: {}", dateFormat.format(new Date()), Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * initialDelay:初始延迟。任务的第一次执行将延迟5秒，然后将以5秒的固定间隔执行。
     */
    @Scheduled(initialDelay = 2000, fixedRate = 5000)
    public void reportCurrentTimeWithInitialDelay() {
        log.info("Fixed Rate Task with Initial Delay: time is {} Current Thread: {}", dateFormat.format(new Date()), Thread.currentThread().getName());
    }

    /**
     * cron：使用Cron表达式。　每分钟的1，2秒运行
     */
    @Scheduled(cron = "1-2 * * * * ? ")
    public void reportCurrentTimeWithCronExpression() {
        log.info("Cron Expression: time is {} Current Thread: {}", dateFormat.format(new Date()), Thread.currentThread().getName());
    }



}
