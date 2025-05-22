package com.example.social_network01.dto;

import com.example.social_network01.dto.message.MessageDTO;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class MessageNotificationDTO {

    private Long id;

    private UserDTO recipient;

    private UserDTO sender;

    private String content;

    private boolean isRead;

    private Instant timestamp;

    private MessageDTO linkedMessage;

}

