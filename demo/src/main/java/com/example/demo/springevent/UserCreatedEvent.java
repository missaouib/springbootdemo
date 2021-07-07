package com.example.demo.springevent;

public class UserCreatedEvent extends BaseEvent<UserEventData> {
    /**
     * @param source    最初触发该事件的对象
     * @param eventData 该类型事件携带的信息
     */
    public UserCreatedEvent(Object source, UserEventData eventData) {
        super(source, eventData);
    }
}
