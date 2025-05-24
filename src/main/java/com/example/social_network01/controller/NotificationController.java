package com.example.social_network01.controller;

import com.example.social_network01.dto.message.MessageNotificationDTO;
import com.example.social_network01.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

//    @MessageMapping("/send-notification")
//    @SendTo("/topic/notifications")
//    public MessageNotificationDTO sendNotification(MessageNotificationDTO message) {
//        return message; // Отправить сообщение всем подписчикам
//    }



//    @GetMapping
//    public ResponseEntity<List<MessageNotificationDTO>> getNotifications(
//            @AuthenticationPrincipal User user,
//            @RequestParam(defaultValue = "50") int limit
//    ) {
//        return ResponseEntity.ok(
//                notificationService.getUserNotifications(user, limit)
//        );
//    }
//
//    @PatchMapping("/{id}/read")
//    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
//        notificationService.markAsRead(id);
//        return ResponseEntity.noContent().build();
//    }
}