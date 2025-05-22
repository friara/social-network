package com.example.social_network01.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class ChatSummaryDTO {
    private Long id;
    private String chatType;
    private String chatName;
    private Instant lastActivity;
    private int unreadCount;
    private String lastMessagePreview;
}