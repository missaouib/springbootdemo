package com.example.demo.controler;

import com.example.demo.entity.User;
import com.example.demo.springevent.UserCreatedEvent;
import com.example.demo.springevent.UserEventData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AccountsController {

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/create")
    public void createUser(){
        publisher.publishEvent(new UserCreatedEvent(this,new UserEventData(new Date())));
    }
}
