package com.example.social_network01.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatDTO {
    private Long id;
    private String chatType;
    private String chatName;
    private LocalDateTime createdWhen;
    private Long createdBy;
}

