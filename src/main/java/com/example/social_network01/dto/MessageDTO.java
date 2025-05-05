package com.example.social_network01.dto;

import com.example.social_network01.model.File;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MessageDTO {
    private Long id;
    private LocalDateTime createdWhen;
    private String text;
    private String status;
    private Long chatId;
    private Long userId;
    private List<FileDTO> files;
}

