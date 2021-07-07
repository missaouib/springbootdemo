package com.example.demo.springevent;

import org.springframework.context.ApplicationEvent;

public abstract class BaseEvent<T> extends ApplicationEvent {

    /**
     * 该类型事件携带的信息
     */
    private T eventData;

    /**
     * @param source 最初触发该事件的对象
     * @param eventData  该类型事件携带的信息
     */
    public BaseEvent(Object source,T eventData) {
        super(source);
        this.eventData = eventData;
    }

    public T getEventData(){
        return eventData;
    }
}
