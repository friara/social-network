package com.example.social_network01.config.web_socket;

import com.example.social_network01.model.User;
import com.example.social_network01.repository.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionInterceptor implements ChannelInterceptor {
    private final ChatMemberRepository chatMemberRepository;

//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
//            String destination = accessor.getDestination();
//            User user = (User) accessor.getUser();
//
//            if (destination.startsWith("/user/queue/messages")) {
//                Long chatId = extractChatId(destination);
//                if (!chatMemberRepository.existsByChatIdAndUserId(chatId, user.getId())) {
//                    throw new AccessDeniedException("Access denied");
//                }
//            }
//        }
//        return message;
//    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return message; // Пропускаем все подписки без проверок
    }

    private Long extractChatId(String destination) {
        String[] parts = destination.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}