package com.example.social_network01.service.message;

import com.example.social_network01.dto.MessageDTO;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageService {
    MessageDTO createMessage(String text, User user, Chat chat, List<MultipartFile> files);
    List<MessageDTO> getAllMessages();
    MessageDTO getMessageById(Long id);
    void deleteMessage(Long id);
    MessageDTO updateMessage(Long id, String newText, List<MultipartFile> newFiles);
}

