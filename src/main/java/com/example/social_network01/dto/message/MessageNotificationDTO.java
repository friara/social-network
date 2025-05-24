package com.example.social_network01.dto.message;

import com.example.social_network01.dto.ChatDTO;
import com.example.social_network01.dto.FileDTO;
import com.example.social_network01.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Schema(name = "MessageNotification", description = "Message notification details")
@Data
public class MessageNotificationDTO {
    @Schema(description = "Unique identifier")
    private Long id;

    @Schema(description = "Sender name")
    private String sender;

    @Schema(description = "Message content")
    private String content;

    @Schema(description = "Read status")
    private boolean isRead;

    @Schema(description = "Timestamp of the message")
    private Instant timestamp;

    @Schema(description = "Chat's name of message")
    private String chatName;

    private Long chatId;
}

