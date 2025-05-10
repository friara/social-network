package com.example.social_network01.service.notification;

import com.example.social_network01.model.MessageNotification;
import com.example.social_network01.model.User;
import com.example.social_network01.model.UserDevice;
import com.example.social_network01.repository.UserDeviceRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final UserDeviceRepository deviceRepo;

    public void sendNotification(User recipient, MessageNotification notification) {
        List<UserDevice> devices = deviceRepo.findByUserId(recipient.getId());

        devices.forEach(device -> {
            com.google.firebase.messaging.Notification fcmNotification =
                    com.google.firebase.messaging.Notification.builder()
                            .setTitle("Новое сообщение")
                            .setBody(notification.getContent())
                            .build();

            com.google.firebase.messaging.Message fcmMessage =
                    com.google.firebase.messaging.Message.builder()
                            .setToken(device.getFcmToken())
                            .setNotification(fcmNotification) // Передаем созданный объект
                            .putData("chatId", notification.getLinkedMessage().getChat().getId().toString())
                            .build();

            FirebaseMessaging.getInstance().sendAsync(fcmMessage);
        });
    }
}