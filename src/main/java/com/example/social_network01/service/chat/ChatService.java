package com.example.social_network01.service.chat;

import com.example.social_network01.dto.ChatDTO;

import java.util.List;

public interface ChatService {
    ChatDTO createChat(ChatDTO chatDTO);
    List<ChatDTO> getAllChats();
    ChatDTO getChatById(Long id);
    void deleteChat(Long id);
}
