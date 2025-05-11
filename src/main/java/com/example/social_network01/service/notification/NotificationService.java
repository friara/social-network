package com.example.social_network01.service.notification;

import com.example.social_network01.dto.MessageDTO;
import com.example.social_network01.dto.MessageNotificationDTO;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.model.MessageNotification;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.MessageNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MessageNotificationRepository notificationRepo;
    private final ModelMapper modelMapper;

    // Получение уведомлений
    public List<MessageNotificationDTO> getUserNotifications(User user, int limit) {
        return notificationRepo.findByRecipientOrderByTimestampDesc(
                user,
                PageRequest.of(0, limit)
        ).stream().map(messageNotification -> modelMapper.map(messageNotification, MessageNotificationDTO.class))
                .collect(Collectors.toList());
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