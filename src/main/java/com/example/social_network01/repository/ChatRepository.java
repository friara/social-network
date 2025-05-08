package com.example.social_network01.repository;

import com.example.social_network01.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    boolean existsByIdAndCreatedBy_Id(Long chatId, Long userId);

    @Query("SELECT CASE WHEN COUNT(cm) > 0 THEN true ELSE false END " +
            "FROM ChatMember cm WHERE cm.chat.id = :chatId AND cm.user.id = :userId")
    boolean existsByIdAndChatMembers_User_Id(
            @Param("chatId") Long chatId,
            @Param("userId") Long userId
    );

    @Query("SELECT c FROM Chat c " +
            "JOIN c.chatMembers cm " +
            "WHERE cm.user.id = :userId " +
            "AND (:search IS NULL OR LOWER(c.chatName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Chat> findUserChatsWithSearch(
            @Param("userId") Long userId,
            @Param("search") String search,
            Pageable pageable);
}

