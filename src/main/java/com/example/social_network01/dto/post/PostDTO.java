package com.example.social_network01.dto.post;

import com.example.social_network01.dto.MediaDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private Long id;

    private LocalDateTime createdWhen;

    @NotBlank(message = "Text cannot be empty")
    private String text;

    private List<MediaDTO> media;

    private Long userId;


}

