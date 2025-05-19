package com.example.social_network01.dto.booking;

import com.example.social_network01.dto.UserDTO;
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
