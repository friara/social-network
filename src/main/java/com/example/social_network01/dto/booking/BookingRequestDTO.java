package com.example.social_network01.dto.booking;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.*;
import java.util.Objects;

@Data
public class BookingRequestDTO {
    @NotNull(message = "ID рабочего пространства обязателен")
    @Positive(message = "ID рабочего пространства должен быть положительным")
    private Long workspaceId;

    @NotNull(message = "Время начала обязательно")
    @FutureOrPresent(message = "Время начала должно быть в будущем или настоящем")
    private Instant bookingStart;

    @NotNull(message = "Время окончания обязательно")
    @Future(message = "Время окончания должно быть в будущем")
    private Instant bookingEnd;

    @AssertTrue(message = "Время окончания должно быть позже времени начала")
    private boolean isBookingEndAfterStart() {
        return bookingEnd.isAfter(bookingStart);
    }

    @AssertTrue(message = "Время начала и окончания должны быть в один день")
    private boolean isSameDay() {
        LocalDate startDate = bookingStart.atZone(ZoneOffset.UTC).toLocalDate();
        LocalDate endDate = bookingEnd.atZone(ZoneOffset.UTC).toLocalDate();
        return Objects.equals(startDate, endDate);
    }

    @AssertTrue(message = "Бронирование возможно только в рабочие дни")
    private boolean isWorkingDay() {
        DayOfWeek dayOfWeek = bookingStart.atZone(ZoneOffset.UTC).getDayOfWeek();
        return dayOfWeek != DayOfWeek.SUNDAY
                && dayOfWeek != DayOfWeek.SATURDAY;
    }
}