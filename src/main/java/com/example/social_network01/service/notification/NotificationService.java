package com.example.social_network01.service.notification;

import com.example.social_network01.dto.message.MessageNotificationDTO;
import com.example.social_network01.dto.message.NotificationOfMessageWithLinks;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.model.*;
import com.example.social_network01.repository.ChatMemberRepository;
import com.example.social_network01.repository.MessageNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MessageNotificationRepository notificationRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void createNotificationForMessage(Message message) {

        User sender = message.getUser();
        Chat chat = message.getChat();

        // Получаем всех участников чата
        List<ChatMember> members = chatMemberRepository.findByChatId(chat.getId());
        members.stream()
                .map(ChatMember::getUser)
                .filter(user -> !user.getId().equals(sender.getId())) // Исключаем отправителя
                .forEach(user -> createNotificationForUser(user, message));
    }

    private void createNotificationForUser(User user, Message message) {
        MessageNotification messageNotification = new MessageNotification();

        messageNotification.setLinkedMessage(message);
        messageNotification.setRecipient(user);
        messageNotification.setSender(message.getUser());

        notificationRepository.save(messageNotification);

    }

    public Long getNotificationsCount(User user) {
        return notificationRepository.countUnreadByRecipient(user);
    }

    // Получение уведомлений
    @Transactional(readOnly = true)
    public List<NotificationOfMessageWithLinks> getUserNotifications(User user, int limit) {
        List<MessageNotification> notifications = notificationRepository.findByRecipientAndStatusOrderByCreatedAtDesc(
                user,
                PageRequest.of(0, limit),
                MessageNotification.NotificationStatus.UNREAD);

        return notifications.stream()
                .map(messageNotification -> toDTO(messageNotification))
                .collect(Collectors.toList());
    }

    private NotificationOfMessageWithLinks toDTO(MessageNotification messageNotification) {
        NotificationOfMessageWithLinks notificationOfMessageWithLinks = new NotificationOfMessageWithLinks();

        notificationOfMessageWithLinks.setId(messageNotification.getId());
        notificationOfMessageWithLinks.setContent(messageNotification.getLinkedMessage().getText());
        if (notificationOfMessageWithLinks.getContent().isEmpty())
            notificationOfMessageWithLinks.setContent("Новое сообщение");
        notificationOfMessageWithLinks.setSender(messageNotification.getSender().getId());
        notificationOfMessageWithLinks.setChatId(messageNotification.getLinkedMessage().getChat().getId());
        notificationOfMessageWithLinks.setTimestamp(messageNotification.getCreatedAt());
        return notificationOfMessageWithLinks;
    }

//    // Получение уведомлений
//    @Transactional(readOnly = true)
//    public List<MessageNotificationDTO> getUserNotifications(User user, int limit) {
//        List<MessageNotification> notifications = notificationRepository.findByRecipientAndStatusOrderByCreatedAtDesc(
//                user,
//                PageRequest.of(0, limit),
//                MessageNotification.NotificationStatus.UNREAD);
//
//        return notifications.stream()
//                .map(messageNotification -> modelMapper.map(messageNotification, MessageNotificationDTO.class))
//                .collect(Collectors.toList());
//    }

//    @Transactional(readOnly = true)
//    public List<MessageNotificationDTO> getUserNotifications(User user, int limit) {
//        // Используйте JOIN FETCH для инициализации связанных сущностей
//        List<MessageNotification> notifications = notificationRepository.findByRecipientAndStatusOrderByCreatedAtDesc(
//                user,
//                PageRequest.of(0, limit),
//                MessageNotification.NotificationStatus.UNREAD
//        );
//
//        return notifications.stream()
//                .map(notification -> {
//                    // Инициализация прокси перед маппингом
//                    Hibernate.initialize(notification.getLinkedMessage());
//                    Hibernate.initialize(notification.getSender());
//                    return modelMapper.map(notification, MessageNotificationDTO.class);
//                })
//                .collect(Collectors.toList());
//    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.updateStatus(
                notificationId,
                MessageNotification.NotificationStatus.READ
        );
    }

    @Transactional
    public void markAllAsReadInChat(Long chatId, User user) {
        notificationRepository.bulkUpdateStatusInChat(
                user,
                chatId,
                MessageNotification.NotificationStatus.UNREAD,
                MessageNotification.NotificationStatus.READ
        );
    }

    @Transactional
    public void markAllAsRead(User user) {
        notificationRepository.bulkUpdateAllForUser(
                user,
                MessageNotification.NotificationStatus.UNREAD,
                MessageNotification.NotificationStatus.READ
        );
    }


}