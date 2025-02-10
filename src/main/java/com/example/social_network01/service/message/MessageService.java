package com.example.social_network01.service.message;

import com.example.social_network01.dto.MessageDTO;

import java.util.List;

public interface MessageService {
    MessageDTO createMessage(MessageDTO messageDTO);
    List<MessageDTO> getAllMessages();
    MessageDTO getMessageById(Long id);
    void deleteMessage(Long id);
}

