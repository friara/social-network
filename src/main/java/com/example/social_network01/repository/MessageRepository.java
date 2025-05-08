package com.example.social_network01.repository;

import com.example.social_network01.model.Chat;
import com.example.social_network01.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chat = :chat ORDER BY m.createdWhen DESC LIMIT 1")
    Optional<Message> findFirstByChatOrderByCreatedAtDesc(@Param("chat") Chat chat);

    @Query("SELECT COUNT(m) FROM Message m " +
            "WHERE m.chat.id = :chatId " +
            "AND m.status = 'UNREAD' " +
            "AND m.user.id <> :userId")
    int countUnreadMessages(@Param("chatId") Long chatId, @Param("userId") Long userId);
}
