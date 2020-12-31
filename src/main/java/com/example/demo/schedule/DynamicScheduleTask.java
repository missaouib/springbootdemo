package com.example.demo.schedule;

import com.example.demo.mapper.CronMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDateTime;

/**
 * 动态：基于接口
 * 从数据库中取得任务执行时间，可以达到实时生效，不需要重启应用
 * 注意： 如果在数据库修改时格式出现错误，则定时任务会停止，即使重新修改正确；此时只能重新启动项目才能恢复。
 */
//@Configuration //主要用于标记配置类，兼备Component的效果。
public class DynamicScheduleTask implements SchedulingConfigurer {

    @Autowired
    private CronMapper cronMapper;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(//1.添加任务内容(Runnable)
                () -> System.out.println("执行动态定时任务: " + LocalDateTime.now().toLocalTime()),
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    String cron = cronMapper.selectByPrimaryKey("1").getCron();
                    //2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        // Omitted Code ..
                    }
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });
    }
}
