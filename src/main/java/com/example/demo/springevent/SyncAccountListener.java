package com.example.demo.springevent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SyncAccountListener {

    @Async
    @EventListener
    public void doSomething(UserCreatedEvent userCreatedEvent){
        log.error(userCreatedEvent.toString());
    }
}
