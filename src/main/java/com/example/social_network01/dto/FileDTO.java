package com.example.social_network01.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDTO {
    private Long id;
    private String fileName;
    private String filePath;
    private String fileType;
    private LocalDateTime uploadedWhen;
    private Long uploadedBy;
}

