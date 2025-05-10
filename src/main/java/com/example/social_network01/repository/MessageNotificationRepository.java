package com.example.social_network01.repository;

import com.example.social_network01.model.MessageNotification;
import com.example.social_network01.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageNotificationRepository extends JpaRepository<MessageNotification, Long> {
    // Метод для получения уведомлений
    List<MessageNotification> findByRecipientOrderByTimestampDesc(
            User recipient,
            Pageable pageable
    );

    // Метод для обновления статуса
    @Modifying
    @Query("UPDATE MessageNotification n SET n.isRead = :isRead WHERE n.id = :id")
    int updateReadStatus(@Param("id") Long id, @Param("isRead") boolean isRead);
}
