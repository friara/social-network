package com.example.social_network01.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Schema(name = "MessageNotification", description = "Message notification details")
@Data
public class NotificationOfMessageWithLinks {
    @Schema(description = "Unique identifier")
    private Long id;

    @Schema(description = "Sender id")
    private Long sender;

    @Schema(description = "Message content")
    private String content;

    @Schema(description = "Timestamp of the message")
    private Instant timestamp;

    @Schema(description = "Chat id")
    private Long chatId;
}
