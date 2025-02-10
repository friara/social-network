package com.example.social_network01.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Long id;
    private LocalDateTime createdWhen;
    private String title;
    private String text;
    private String imageName;
    private Long userId;
}

