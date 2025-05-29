package com.example.social_network01.service.message;

import com.example.social_network01.dto.message.MessageDTO;
import com.example.social_network01.dto.message.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    MessageDTO getMessageById(Long id);
    Page<MessageDTO> getMessagesByChatId(Long chatId, Pageable pageable);
    MessageDTO createMessage(Long chatId, Long userId, MessageRequestDTO request);
    MessageDTO updateMessage(Long messageId, Long userId, MessageRequestDTO request);
    void deleteMessage(Long chatId, Long messageId);

    boolean isMessageAuthor(Long messageId, Long userId);
}

