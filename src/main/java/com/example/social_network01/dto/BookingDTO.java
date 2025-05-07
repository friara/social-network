package com.example.social_network01.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDTO {
    Long id;
    Long workspaceId;
    Long userId;
    LocalDateTime bookingStart;
    LocalDateTime bookingEnd;
    LocalDateTime createdWhen;

}
