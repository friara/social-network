package com.example.social_network01.controller;

import com.example.social_network01.dto.MessageDTO;
import com.example.social_network01.dto.message.MessageCreateRequest;
import com.example.social_network01.dto.message.MessageUpdateRequest;
import com.example.social_network01.exception.custom.ChatNotFoundException;
import com.example.social_network01.exception.custom.MessageNotFoundException;
import com.example.social_network01.exception.custom.UserNotFoundException;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.ChatRepository;
import com.example.social_network01.repository.UserRepository;
import com.example.social_network01.service.message.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats/{chatId}/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    @PreAuthorize("@chatService.isUserParticipant(#chatId, #currentUser.id)")
    public ResponseEntity<Page<MessageDTO>> getChatMessages(
            @PathVariable Long chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdWhen,desc") String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        return ResponseEntity.ok(messageService.getMessagesByChatId(chatId, pageable));
    }

    @PostMapping
    @PreAuthorize("@chatService.isUserParticipant(#chatId, #currentUser.id)")
    public ResponseEntity<MessageDTO> createMessage(
            @PathVariable Long chatId,
            @RequestBody @Valid MessageCreateRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.createMessage(chatId, currentUser.getId(), request));
    }

    @PutMapping("/{messageId}")
    @PreAuthorize("@messageService.isMessageAuthor(#messageId, #currentUser.id)")
    public ResponseEntity<MessageDTO> updateMessage(
            @PathVariable Long chatId,
            @PathVariable Long messageId,
            @ModelAttribute MessageUpdateRequest updateRequest,
            @AuthenticationPrincipal User currentUser) {

        MessageDTO updatedMessage = messageService.updateMessage(
                messageId,
                currentUser.getId(),
                updateRequest
        );
        return ResponseEntity.ok(updatedMessage);
    }

    @GetMapping("/{messageId}")
    @PreAuthorize("@chatService.isUserParticipant(#chatId, #currentUser.id)")
    public ResponseEntity<MessageDTO> getMessage(
            @PathVariable Long chatId,
            @PathVariable Long messageId) {

        return ResponseEntity.ok(messageService.getMessageById(messageId));
    }

    @DeleteMapping("/{messageId}")
    @PreAuthorize("@messageService.isMessageAuthor(#messageId, #currentUser.id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long chatId,
            @PathVariable Long messageId) {

        messageService.deleteMessage(chatId, messageId);
        return ResponseEntity.noContent().build();
    }

}