package com.example.social_network01.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageNotificationDTO {

    private Long id;

    private UserDTO recipient;

    private UserDTO sender;

    private String content;

    private boolean isRead;

    private LocalDateTime timestamp;

    private MessageDTO linkedMessage;

}

