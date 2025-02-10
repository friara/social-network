package com.example.social_network01.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private LocalDateTime createdWhen;
    private String text;
    private String status;
    private Long chatId;
    private Long userId;
}

