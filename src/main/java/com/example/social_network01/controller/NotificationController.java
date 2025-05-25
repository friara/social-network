package com.example.social_network01.controller;

import com.example.social_network01.dto.message.MessageNotificationDTO;
import com.example.social_network01.dto.message.NotificationOfMessageWithLinks;
import com.example.social_network01.model.User;
import com.example.social_network01.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notifications")
public class NotificationController {
    private final NotificationService notificationService;


    @GetMapping
    public ResponseEntity<List<NotificationOfMessageWithLinks>> getNotifications(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(notificationService.getUserNotifications(user, limit));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getNotificationsCount(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(notificationService.getNotificationsCount(user));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal User user) {
        notificationService.markAllAsRead(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/chat/{id}/read")
    public ResponseEntity<Void> markAsReadForChat(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        notificationService.markAllAsReadInChat(id, user);
        return ResponseEntity.ok().build();
    }
}