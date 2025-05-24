package com.example.social_network01.config.web_socket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class WebSocketLoggingInterceptor implements ChannelInterceptor {
    private static final Logger log = LoggerFactory.getLogger(WebSocketLoggingInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {
            log.info("[WebSocket] Client connected: {}", accessor.getSessionId());
        } else if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
            log.info("[WebSocket] Subscribe to: {}", accessor.getDestination());
        } else if (accessor.getCommand() == StompCommand.SEND) {
            log.info("[WebSocket] Message sent to: {}", accessor.getDestination());
        }

        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.DISCONNECT) {
            log.info("[WebSocket] Client disconnected: {}", accessor.getSessionId());
        }
    }
}
