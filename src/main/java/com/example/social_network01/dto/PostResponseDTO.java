package com.example.social_network01.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponseDTO {
    private Long id;
    private LocalDateTime createdWhen;
    private String text;
    private List<MediaDTO> media;
    private Long userId;
    private Long likeCount;
    private Long commentCount;
    private Long repostCount;
    private boolean liked;
}
