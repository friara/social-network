package com.example.social_network01.dto;

import com.example.social_network01.model.Media.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "DTO для представления медиа-вложений")
public class MediaDTO {
    @Schema(description = "Уникальный идентификатор медиа", example = "123")
    private Long id;

    @Schema(description = "Тип медиа-контента", example = "IMAGE")
    private MediaType mediaType;

    @Schema(description = "MIME-тип файла", example = "image/png")
    private String mimeType;

    @Schema(description = "Оригинальное имя файла", example = "cat.png")
    private String fileName;

    @Schema(description = "URL для скачивания файла", example = "/api/media/123")
    private String downloadUrl;

    @Schema(description = "Размер файла в байтах", example = "204800")
    private Long fileSize;

    @Schema(description = "Дата и время загрузки", example = "2023-07-20T15:30:00")
    private LocalDateTime uploadedWhen;

    @Schema(description = "ID связанного поста", example = "456")
    private Long postId;
}
