package com.example.social_network01.service.notification;

import com.example.social_network01.model.Chat;
import com.example.social_network01.model.ChatMember;
import com.example.social_network01.model.MessageNotification;
import com.example.social_network01.model.events.NewMessageEvent;
import com.example.social_network01.repository.ChatMemberRepository;
import com.example.social_network01.repository.MessageNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageNotificationHandler {
    private final ChatMemberRepository chatMemberRepo;
    private final MessageNotificationRepository notificationRepo;
    private final FcmService fcmService;

    @Async
    @EventListener
    public void handleNewMessage(NewMessageEvent event) {
        Chat chat = event.getMessage().getChat();
        List<ChatMember> members = chatMemberRepo.findByChatId(chat.getId());

        members.stream()
                .filter(m -> !m.getUser().getId().equals(event.getSenderId()))
                .forEach(member -> {
                    // Создание уведомления
                    MessageNotification notification = new MessageNotification();
                    notification.setRecipient(member.getUser());
                    notification.setSender(event.getMessage().getUser());
                    notification.setContent(event.getMessage().getText());
                    notification.setLinkedMessage(event.getMessage());
                    notificationRepo.save(notification);

                    // Отправка FCM
                    fcmService.sendNotification(member.getUser(), notification);
                });
    }
}