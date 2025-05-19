package com.example.social_network01.dto;

import com.example.social_network01.dto.booking.BookingDTO;
import lombok.Data;

import java.util.List;

@Data
public class WorkspaceDTO {
    Long id;
    String workspaceName;
    boolean isAvailable;
    List<BookingDTO> currentBookings;
}
