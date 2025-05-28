package com.example.social_network01.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public record UpdateChatNameRequest(
        @Getter
        @NotBlank(message = "Chat name cannot be blank")
        @Size(min = 1, max = 255, message = "Chat name must be between 1-100 characters")
        String newName
) {}
