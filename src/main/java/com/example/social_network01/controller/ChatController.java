package com.example.social_network01.controller;

import com.example.social_network01.dto.ChatDTO;
import com.example.social_network01.dto.ChatSummaryDTO;
import com.example.social_network01.exception.custom.*;
import com.example.social_network01.model.User;
import com.example.social_network01.service.chat.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatDTO> createChat(
            @RequestBody @Valid ChatDTO chatDTO,
            @AuthenticationPrincipal User currentUser) {
        ChatDTO createdChat = chatService.createChat(chatDTO, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChat);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@chatService.isChatCreator(#id, #currentUser.id)")
    public ResponseEntity<ChatDTO> updateChat(
            @PathVariable Long id,
            @RequestBody @Valid ChatDTO chatDTO) {
        ChatDTO updatedChat = chatService.updateChat(id, chatDTO);
        return ResponseEntity.ok(updatedChat);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<ChatSummaryDTO>> getMyChats(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdWhen").descending());
        return ResponseEntity.ok(chatService.getUserChats(currentUser.getId(), search, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@chatService.isUserParticipant(#id, #currentUser.id)")
    public ResponseEntity<ChatDTO> getChatById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(chatService.getChatById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@chatService.isChatCreator(#id, #currentUser.id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }


}