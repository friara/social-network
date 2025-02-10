package com.example.social_network01.dto;

import lombok.Data;

@Data
public class RepostDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private Long targetChatId;
}
