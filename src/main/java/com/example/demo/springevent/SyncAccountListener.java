package com.example.demo.springevent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SyncAccountListener {

    @EventListener
    public void doSomething(UserCreatedEvent userCreatedEvent){
        System.out.println(userCreatedEvent.getTimestamp());
    }
}
