package com.example.social_network01.controller;

import com.example.social_network01.model.Chat;
import com.example.social_network01.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @GetMapping
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        return chatRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Chat createChat(@RequestBody Chat chat) {
        return chatRepository.save(chat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chat> updateChat(@PathVariable Long id, @RequestBody Chat chatDetails) {
        return chatRepository.findById(id)
                .map(chat -> {
                    chat.setChatType(chatDetails.getChatType());
                    chat.setChatName(chatDetails.getChatName());
                    return ResponseEntity.ok(chatRepository.save(chat));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        return chatRepository.findById(id)
                .map(chat -> {
                    chatRepository.delete(chat);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
