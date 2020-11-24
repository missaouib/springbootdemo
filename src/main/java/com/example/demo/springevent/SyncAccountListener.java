package com.example.demo.springevent;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class SyncAccountListener {

    @Autowired
    private UserService userService;

    /**
     * Listener默认都是同步的
     * @param userCreatedEvent
     */
    @Async("taskExecutorA")
    @EventListener
    public void doSomething(UserCreatedEvent userCreatedEvent){
        log.error(userCreatedEvent.toString());
    }


    /**
     * 可以指定监听器和发布事件的方法的事务隔离级别。隔离级别确保数据的有效性。@TransactionalEventListener注解会将监听器声明为一个事务管理器
     * 当一个监听器方法被@TransactionalEventListener注解时，那么这个监听器只会在调用方为事务方法时执行，如果调用方是非事务方法，
     * 则无法该监听器不会被通知。值得注意的是，虽然@TransactionalEventListener带有Transaction关键字，
     * 但这个方法并没有声明监听器为Transactional的。
     *
     * 该注解还提供了另外的两个属性fallbackExecution和phase。
     * 定义：fallbackExecution 设置Listener是否要在没有事务的情况下处理event。
     * 默认为false，表示当publishEvent所在方法没有事务控制时，该监听器不监听事件。通过设置fallbackExecution=true，可以让Listener在任何情况都可以执行。
     */
    @Async("taskExecutorA")
    @Transactional(rollbackFor = Exception.class)
    @TransactionalEventListener(fallbackExecution = false,phase = TransactionPhase.AFTER_COMMIT )
    public void method1(UserCreatedEvent userCreatedEvent){
        User user = new User();
        user.setPassword("123");
        user.setUsername("123");
        int insert = userService.insert(user);
        log.error(String.valueOf(insert));
        log.error(userCreatedEvent.toString());
    }
}
