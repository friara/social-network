package com.example.social_network01.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private LocalDateTime createdWhen;
    private String title;
    private String text;
    private List<String> mediaUrls; // Список ссылок на медиафайлы
    private Long userId;
}

