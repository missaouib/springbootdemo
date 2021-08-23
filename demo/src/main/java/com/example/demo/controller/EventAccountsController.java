package com.example.demo.controller;

import com.example.demo.springevent.UserCreatedEvent;
import com.example.demo.springevent.UserEventData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class EventAccountsController {

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public void createUser() {
        publisher.publishEvent(new UserCreatedEvent(this, new UserEventData(new Date())));
    }
}
