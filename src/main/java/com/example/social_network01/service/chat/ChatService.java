package com.example.social_network01.service.chat;

import com.example.social_network01.dto.ChatDTO;
import com.example.social_network01.dto.ChatSummaryDTO;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface ChatService {
    ChatDTO createChat(ChatDTO chatDTO);

    ChatDTO createChat(ChatDTO chatDTO, User user);

    List<ChatDTO> getAllChats();

    ChatDTO getChatById(Long id);

    void deleteChat(Long id);

    ChatDTO updateChat(Long id, ChatDTO chatDTO);

    boolean isChatCreator(Long chatId, Long userId);

    boolean isUserParticipant(Long chatId, Long userId);

    Page<ChatSummaryDTO> getUserChats(Long userId, String search, Pageable pageable);
}
