package com.example.social_network01.model.events;

import com.example.social_network01.model.Message;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewMessageEvent extends ApplicationEvent {
    private final Message message;
    private final Long senderId;

    public NewMessageEvent(Object source, Message message, Long senderId) {
        super(source);
        this.message = message;
        this.senderId = senderId;
    }
}