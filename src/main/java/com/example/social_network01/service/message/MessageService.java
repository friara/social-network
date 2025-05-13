package com.example.social_network01.service.message;

import com.example.social_network01.dto.MessageDTO;
import com.example.social_network01.dto.message.*;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageService {
//    MessageDTO createMessage(String text, User user, Chat chat, List<MultipartFile> files);
//    List<MessageDTO> getAllMessages();
    MessageDTO getMessageById(Long id);
//    void deleteMessage(Long id);
//    MessageDTO updateMessage(Long id, String newText, List<MultipartFile> newFiles);
    Page<MessageDTO> getMessagesByChatId(Long chatId, Pageable pageable);
    MessageDTO createMessage(Long chatId, Long userId, MessageCreateRequest request);
    MessageDTO updateMessage(Long messageId, Long userId, MessageUpdateRequest request);
    void deleteMessage(Long messageId, Long userId);

    boolean isMessageAuthor(Long messageId, Long userId);
}

