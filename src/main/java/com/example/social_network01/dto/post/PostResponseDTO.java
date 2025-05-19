package com.example.social_network01.dto.post;

import com.example.social_network01.dto.MediaDTO;
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
    private Integer likeCount;
    private Integer commentCount;
    private Integer repostCount;
    private boolean liked;
}
