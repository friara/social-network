package com.example.social_network01.dto.post;

import com.example.social_network01.dto.MediaDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "DTO для поста")
public class PostDTO {

    @Schema(
            description = "Уникальный идентификатор поста",
            example = "123",
            type = "integer"
    )
    private Long id;

    @Schema(
            description = "Дата и время создания поста",
            example = "2023-01-01T12:00:00",
            type = "string",
            format = "date-time"
    )
    private Instant createdWhen;


    @Schema(
            description = "Текст поста",
            example = "Привет, мир!"
    )
    private String text;

    @ArraySchema(schema = @Schema(implementation = MediaDTO.class))
    private List<MediaDTO> media;

    @Schema(
            description = "Идентификатор автора поста",
            example = "456",
            type = "integer"
    )
    private Long userId;
}