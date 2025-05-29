package com.example.social_network01.controller;

import com.example.social_network01.dto.chat.ChatDTO;
import com.example.social_network01.dto.chat.ChatSummaryDTO;
import com.example.social_network01.dto.chat.UpdateChatNameRequest;
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

import java.util.List;

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
            @RequestBody @Valid ChatDTO chatDTO,
            @AuthenticationPrincipal User currentUser) {
        ChatDTO updatedChat = chatService.updateChat(id, chatDTO, currentUser);
        return ResponseEntity.ok(updatedChat);
    }

    // Добавление участников в чат
    @PostMapping("/{chatId}/participants")
    @PreAuthorize("@chatService.isChatCreator(#chatId, #currentUser.id)")
    public ResponseEntity<ChatDTO> addParticipants(
            @PathVariable Long chatId,
            @RequestBody @Valid List<Long> userIds,
            @AuthenticationPrincipal User currentUser) {

        ChatDTO updatedChat = chatService.addParticipants(chatId, userIds);
        return ResponseEntity.ok(updatedChat);
    }

    // Удаление участника из чата
    @DeleteMapping("/{chatId}/participants/{userId}")
    @PreAuthorize("@chatService.isChatCreator(#chatId, #currentUser.id)")
    public ResponseEntity<ChatDTO> removeParticipant(
            @PathVariable Long chatId,
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser) {

        ChatDTO updatedChat = chatService.deleteParticipant(chatId, userId);
        return ResponseEntity.ok(updatedChat);
    }

    // Выход участника из чата
    @DeleteMapping("/{chatId}/exit")
    public ResponseEntity<ChatDTO> exitFromChat(
            @PathVariable Long chatId,
            @AuthenticationPrincipal User currentUser) {

        ChatDTO updatedChat = chatService.deleteParticipant(chatId, currentUser.getId());
        return ResponseEntity.ok(updatedChat);
    }

    @PatchMapping("/{id}/name")
    @PreAuthorize("@chatService.isChatCreator(#id, #currentUser.id)")
    public ResponseEntity<ChatDTO> updateChatName(
            @PathVariable Long id,
            @RequestBody @Valid UpdateChatNameRequest request,
            @AuthenticationPrincipal User currentUser) {
        ChatDTO updatedChat = chatService.updateChatName(id, request.getNewName());
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
        return ResponseEntity.ok(chatService.getChatById(id, currentUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@chatService.isChatCreator(#id, #currentUser.id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }


}