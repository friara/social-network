package com.example.social_network01.dto.booking;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class BookingRequestDTO {
    @NotNull(message = "ID рабочего пространства обязателен")
    @Positive(message = "ID рабочего пространства должен быть положительным")
    private Long workspaceId;

    @NotNull(message = "Время начала обязательно")
    @FutureOrPresent(message = "Время начала должно быть в будущем или настоящем")
    private LocalDateTime bookingStart;

    @NotNull(message = "Время окончания обязательно")
    @Future(message = "Время окончания должно быть в будущем")
    private LocalDateTime bookingEnd;

    @AssertTrue(message = "Время окончания должно быть позже времени начала")
    private boolean isBookingEndAfterStart() {
        return bookingEnd.isAfter(bookingStart);
    }
}