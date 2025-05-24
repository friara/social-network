package com.example.social_network01.service.notification;

import com.example.social_network01.config.web_socket.WebSocketLoggingInterceptor;
import com.example.social_network01.dto.UserDTO;
import com.example.social_network01.dto.message.MessageDTO;
import com.example.social_network01.dto.message.MessageNotificationDTO;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.ChatMember;
import com.example.social_network01.model.Message;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatNotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMemberCacheHelper chatMemberCacheHelper;
    private final ModelMapper modelMapper;

    private static final Logger log = LoggerFactory.getLogger(ChatNotificationService.class);

    public void notifyChatMembers(Message message) {
        User sender = message.getUser();
        Chat chat = message.getChat();

        // Получаем всех участников чата
        List<ChatMember> members = chatMemberCacheHelper.getCachedChatMembers(message.getChat().getId());
        MessageNotificationDTO dto = messageToDto(message);
        members.stream()
                .map(ChatMember::getUser)
                .filter(user -> !user.getId().equals(sender.getId())) // Исключаем отправителя
                .forEach(user -> sendToUser(user, dto));

    }

    //@Asyncd
    public void sendToUser(User user, MessageNotificationDTO dto) {
        log.info("[WebSocket] Sent to user {}: {}", user.getUsername(), dto);
        messagingTemplate.convertAndSendToUser(
                user.getUsername(),
                "/queue/messages",
                dto
        );
    }

    private MessageNotificationDTO messageToDto(Message message) {
        MessageNotificationDTO notification =  new MessageNotificationDTO();
        try {
            notification.setId(message.getId());
            notification.setSender(message.getUser().getFirstName() + " " + message.getUser().getLastName());
            notification.setRead(false);
            notification.setTimestamp(message.getCreatedWhen());
            notification.setChatName(message.getChat().getChatName());
            notification.setChatId(message.getChat().getId());
            String content = message.getText();
            if (content.isEmpty() && !message.getFiles().isEmpty()) {
                content = message.getFiles().get(0).getFileName();
            }

            notification.setContent(content);
        } catch (Exception e) {
            notification.setContent("[New message]");
        }

        return notification;
    }
}
