package com.example.social_network01.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private Instant createdWhen;
    private Long postId;
    private Long userId;
    private String text;
    private Long answerToComm;
}

