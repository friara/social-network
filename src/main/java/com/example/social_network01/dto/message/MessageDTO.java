package com.example.social_network01.dto.message;

import com.example.social_network01.dto.FileDTO;
import com.example.social_network01.dto.MediaDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @ArraySchema(schema = @Schema(implementation = FileDTO.class))
    private List<FileDTO> files;
}

