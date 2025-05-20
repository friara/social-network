package com.example.social_network01.service.notification;

import com.example.social_network01.model.User;
import com.example.social_network01.model.UserDevice;
import com.example.social_network01.repository.UserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDeviceService {
    private final UserDeviceRepository deviceRepo;

    // Сохранение или обновление токена устройства
    @Transactional
    public void saveOrUpdateDevice(User user, String fcmToken, String deviceId) {
        // Поиск устройства по ID
        Optional<UserDevice> existingDevice = deviceRepo.findByDeviceId(deviceId);

        if (existingDevice.isPresent()) {
            // Обновление существующего устройства
            UserDevice device = existingDevice.get();
            device.setFcmToken(fcmToken);
            device.setLastActive(LocalDateTime.now());
        } else {
            // Создание новой записи
            UserDevice device = new UserDevice();
            device.setUser(user);
            device.setFcmToken(fcmToken);
            device.setDeviceId(deviceId);
            deviceRepo.save(device);
        }
    }

    // Получение всех активных токенов пользователя
    public List<String> getActiveFcmTokens(User user) {
        return deviceRepo.findAllByUser(user).stream()
                .filter(device -> device.getLastActive().isAfter(LocalDateTime.now().minusDays(30)))
                .map(UserDevice::getFcmToken)
                .collect(Collectors.toList());
    }

    // Удаление неактивных устройств
    @Scheduled(cron = "0 0 3 * * ?") // Каждый день в 3:00
    public void cleanupInactiveDevices() {
        LocalDateTime cutoff = LocalDateTime.now().minusMonths(1);
        deviceRepo.deleteByLastActiveBefore(cutoff);
    }
}