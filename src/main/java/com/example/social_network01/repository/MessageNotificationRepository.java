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
    List<MessageNotification> findByRecipientOrderByCreatedAtDesc(
            User recipient,
            Pageable pageable
    );

    List<MessageNotification> findByRecipientAndStatusOrderByCreatedAtDesc(
            User recipient,
            Pageable pageable,
            MessageNotification.NotificationStatus status
    );

    // Метод для обновления статуса
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE MessageNotification n SET n.status = :status WHERE n.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") MessageNotification.NotificationStatus status);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE MessageNotification n 
        SET n.status = :newStatus 
        WHERE n.recipient = :user 
        AND n.linkedMessage.chat.id = :chatId 
        AND n.status = :oldStatus
    """)
    int bulkUpdateStatusInChat(
            @Param("user") User user,
            @Param("chatId") Long chatId,
            @Param("oldStatus") MessageNotification.NotificationStatus oldStatus,
            @Param("newStatus") MessageNotification.NotificationStatus newStatus
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE MessageNotification n 
        SET n.status = :newStatus 
        WHERE n.recipient = :user 
        AND n.status = :oldStatus
    """)
    int bulkUpdateAllForUser(
            @Param("user") User user,
            @Param("oldStatus") MessageNotification.NotificationStatus oldStatus,
            @Param("newStatus") MessageNotification.NotificationStatus newStatus
    );

    @Query("SELECT COUNT(n) FROM MessageNotification n WHERE n.recipient = :user AND n.status = 'UNREAD'")
    long countUnreadByRecipient(@Param("user") User user);
}
