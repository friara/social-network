package com.example.social_network01.service.chat;

import com.example.social_network01.dto.chat.ChatDTO;
import com.example.social_network01.dto.chat.ChatSummaryDTO;
import com.example.social_network01.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {
    ChatDTO createChat(ChatDTO chatDTO);

    ChatDTO createChat(ChatDTO chatDTO, User user);

    List<ChatDTO> getAllChats();

    ChatDTO getChatById(Long id, User user);

    void deleteChat(Long id);

    ChatDTO updateChat(Long id, ChatDTO chatDTO, User user);

    boolean isChatCreator(Long chatId, Long userId);

    boolean isUserParticipant(Long chatId, Long userId);

    Page<ChatSummaryDTO> getUserChats(Long userId, String search, Pageable pageable);

    ChatDTO deleteParticipant(Long chatId, Long userId);

    ChatDTO addParticipants(Long chatId, List<Long> userIds);

    ChatDTO updateChatName(Long chatId, String newName);
}
