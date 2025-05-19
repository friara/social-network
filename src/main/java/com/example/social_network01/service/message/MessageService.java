package com.example.social_network01.service.message;

import com.example.social_network01.dto.message.MessageDTO;
import com.example.social_network01.dto.message.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
//    MessageDTO createMessage(String text, User user, Chat chat, List<MultipartFile> files);
//    List<MessageDTO> getAllMessages();
    MessageDTO getMessageById(Long id);
//    void deleteMessage(Long id);
//    MessageDTO updateMessage(Long id, String newText, List<MultipartFile> newFiles);
    Page<MessageDTO> getMessagesByChatId(Long chatId, Pageable pageable);
    MessageDTO createMessage(Long chatId, Long userId, MessagRequestDTO request);
    MessageDTO updateMessage(Long messageId, Long userId, MessagRequestDTO request);
    void deleteMessage(Long messageId, Long userId);

    boolean isMessageAuthor(Long messageId, Long userId);
}

