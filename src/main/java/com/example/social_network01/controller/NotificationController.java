package com.example.social_network01.controller;

import com.example.social_network01.model.MessageNotification;
import com.example.social_network01.model.User;
import com.example.social_network01.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<MessageNotification>> getNotifications(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "50") int limit
    ) {
        return ResponseEntity.ok(
                notificationService.getUserNotifications(user, limit)
        );
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}