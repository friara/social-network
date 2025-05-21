package com.example.social_network01.dto;

import com.example.social_network01.dto.booking.BookingDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class WorkspaceDTO {
    Long id;
    String workspaceName;
    boolean isAvailable;
    @ArraySchema(schema = @Schema(implementation = BookingDTO.class))
    List<BookingDTO> currentBookings;
}
