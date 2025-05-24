//package com.example.social_network01.handler;
//
//import com.example.social_network01.dto.message.MessageNotificationDTO;
//import com.example.social_network01.model.Chat;
//import com.example.social_network01.model.ChatMember;
//import com.example.social_network01.model.Message;
//import com.example.social_network01.model.User;
//import com.example.social_network01.model.events.NewMessageEvent;
//import com.example.social_network01.repository.ChatMemberRepository;
//import com.example.social_network01.service.notification.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//// 1. Обновленный обработчик событий (без FCM)
//@Component
//@RequiredArgsConstructor
//public class NewMessageEventHandler {
//    private final NotificationService notificationService;
//    private final ChatMemberRepository chatMemberRepository;
//    private final SimpMessagingTemplate messagingTemplate;
//    private final ModelMapper modelMapper;
//
//    @EventListener
//    @Transactional
//    public void handleNewMessageEvent(NewMessageEvent event) {
//        Message message = event.getMessage();
//        Chat chat = message.getChat();
//        User sender = message.getUser();
//
//        List<ChatMember> members = chatMemberRepository.findByChatId(chat.getId());
//
//        members.stream()
//                .filter(m -> !m.getUser().getId().equals(event.getSenderId()))
//                .forEach(member -> {
//                    // Создание уведомления
//                    MessageNotification notification = new MessageNotification();
//                    notification.setRecipient(member.getUser());
//                    notification.setSender(event.getMessage().getUser());
//                    notification.setContent(event.getMessage().getText());
//                    notification.setLinkedMessage(event.getMessage());
//                    notificationRepo.save(notification);
//
//                    processRecipient(message, member);
//                });
//
//        chat.getParticipants().stream()
//                .filter(participant -> !participant.getId().equals(sender.getId()))
//                .forEach(recipient -> processRecipient(message, recipient));
//    }
//
//    private void processRecipient(Message message, User recipient) {
//        MessageNotificationDTO notification = createNotification(message, recipient);
//        MessageNotificationDTO savedNotification = notificationService.createNotification(notification);
//
//        sendWebSocketNotification(recipient, savedNotification);
//    }
//
//    private MessageNotificationDTO createNotification(Message message, User recipient) {
//        MessageNotificationDTO dto = new MessageNotificationDTO();
//        dto.setRecipient(modelMapper.map(recipient, UserDTO.class));
//        dto.setSender(modelMapper.map(message.getUser(), UserDTO.class));
//        dto.setContent(message.getText());
//        dto.setLinkedMessage(modelMapper.map(message, MessageDTO.class));
//        return dto;
//    }
//
//    private void sendWebSocketNotification(User recipient, MessageNotificationDTO notification) {
//        messagingTemplate.convertAndSendToUser(
//                recipient.getId().toString(),
//                "/queue/notifications",
//                notification
//        );
//    }
//}