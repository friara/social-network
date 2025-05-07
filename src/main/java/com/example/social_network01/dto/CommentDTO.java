package com.example.social_network01.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private LocalDateTime createdWhen;
    private Long postId;
    private Long userId;
    private String text;
    private String answerToComm;
}

