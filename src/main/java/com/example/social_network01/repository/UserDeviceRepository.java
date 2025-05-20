package com.example.social_network01.repository;

import com.example.social_network01.model.User;
import com.example.social_network01.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    List<UserDevice> findByUserId(Long userId);
    // Найти все устройства пользователя
    List<UserDevice> findAllByUser(User user);

    // Найти устройство по FCM-токену
    Optional<UserDevice> findByFcmToken(String fcmToken);

    // Найти устройство по ID устройства
    Optional<UserDevice> findByDeviceId(String deviceId);

    // Удаление устройств, неактивных дольше указанного времени
    @Modifying
    @Query("DELETE FROM UserDevice d WHERE d.lastActive < :cutoff")
    void deleteByLastActiveBefore(@Param("cutoff") LocalDateTime cutoff);
}
