package com.example.social_network01.dto.chat;

import lombok.Data;

@Data
public class ChatMemberDTO {
    private Long id;
    private Long chatId;
    private Long userId;
}

