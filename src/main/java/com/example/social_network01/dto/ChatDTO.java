package com.example.social_network01.dto;

import com.example.social_network01.model.ChatMember;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatDTO {
    private Long id;

    @NotBlank(message = "Chat type is required")
    private String chatType;

    @Size(min = 2, max = 50, message = "Chat name must be between 2 and 50 characters")
    private String chatName;

    private LocalDateTime createdWhen;

    @NotNull(message = "Creator ID is required")
    private Long createdBy;

    @NotEmpty(message = "Participants are required")
    private List<Long> participantIds;

    private Long otherUserId; // Only for private chats

    @AssertTrue(message = "Other user ID is required for private chats")
    private boolean isPrivateChatValid() {
        return !"PRIVATE".equals(chatType) || otherUserId != null;
    }
}
