package com.example.social_network01.controller;

import com.example.social_network01.dto.MessageDTO;
import com.example.social_network01.exception.custom.ChatNotFoundException;
import com.example.social_network01.exception.custom.MessageNotFoundException;
import com.example.social_network01.exception.custom.UserNotFoundException;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.ChatRepository;
import com.example.social_network01.repository.UserRepository;
import com.example.social_network01.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository; // Добавляем репозиторий
    private final ChatRepository chatRepository; // Добавляем репозиторий

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(
            @RequestParam String text,
            @RequestParam(required = false) List<MultipartFile> files,
            @AuthenticationPrincipal User currentUser, // Заменяем Principal
            @RequestParam Long chatId) {

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Chat not found"));

        MessageDTO messageDTO = messageService.createMessage(text, user, chat, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO> updateMessage(
            @PathVariable Long id,
            @RequestParam String newText,
            @RequestParam(required = false) List<MultipartFile> newFiles,
            @AuthenticationPrincipal User currentUser) { // Добавляем проверку прав

        MessageDTO message = messageService.getMessageById(id);

        // Проверяем, что пользователь - автор сообщения
        if (!message.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can't edit this message");
        }

        MessageDTO updatedMessage = messageService.updateMessage(id, newText, newFiles);
        return ResponseEntity.ok(updatedMessage);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<MessageDTO> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(messageService.getMessageById(id));
        } catch (MessageNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {

        MessageDTO message = messageService.getMessageById(id);

        // Проверяем, что пользователь - автор сообщения
        if (!message.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can't edit this message");
        }

        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

}