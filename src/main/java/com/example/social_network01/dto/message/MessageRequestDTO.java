package com.example.social_network01.dto.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MessageRequestDTO {

    private String text;

    private List<MultipartFile> files;
}

