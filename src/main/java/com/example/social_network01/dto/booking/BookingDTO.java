package com.example.social_network01.dto.booking;

import com.example.social_network01.dto.UserDTO;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class BookingDTO {
    Long id;
    Long workspaceId;
    Long userId;
    Instant bookingStart;
    Instant bookingEnd;
    Instant createdWhen;
}
