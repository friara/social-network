package com.example.social_network01.service.notification;

import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.model.MessageNotification;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.MessageNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MessageNotificationRepository notificationRepo;

    // Получение уведомлений
    public List<MessageNotification> getUserNotifications(User user, int limit) {
        return notificationRepo.findByRecipientOrderByTimestampDesc(
                user,
                PageRequest.of(0, limit)
        );
    }

    // Пометка как прочитанного
    @Transactional
    public void markAsRead(Long notificationId) {
        int updatedRows = notificationRepo.updateReadStatus(notificationId, true);
        if (updatedRows == 0) {
            throw new ResourceNotFoundException("Notification not found");
        }
    }
}