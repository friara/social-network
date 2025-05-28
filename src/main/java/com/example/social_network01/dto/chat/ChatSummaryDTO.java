package com.example.social_network01.dto.chat;

import lombok.Data;

import java.time.Instant;

@Data
public class ChatSummaryDTO {
    private Long id;
    private String chatType;
    private String chatName;
    private Instant lastActivity;
    private int unreadCount;
    private String lastMessagePreview;
}