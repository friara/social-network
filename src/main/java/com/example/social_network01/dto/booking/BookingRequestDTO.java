package com.example.social_network01.dto.booking;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
//
//    // Проверка, что время начала не раньше 9:00
//    @AssertTrue(message = "Время начала должно быть не раньше 09:00")
//    private boolean isBookingStartTimeValid() {
//        return !bookingStart.toLocalTime().isBefore(LocalTime.of(9, 0))
//                && !bookingStart.toLocalTime().isAfter(LocalTime.of(18, 0));
//    }
//
//    // Проверка, что время окончания не позже 18:00
//    @AssertTrue(message = "Время окончания должно быть не позже 18:00")
//    private boolean isBookingEndTimeValid() {
//        return !bookingEnd.toLocalTime().isAfter(LocalTime.of(18, 0))
//                && !bookingEnd.toLocalTime().isBefore(LocalTime.of(9, 0));
//    }

    // Проверка, что бронирование в рамках одного дня
    @AssertTrue(message = "Время начала и окончания должны быть в один день")
    private boolean isSameDay() {
        return bookingStart.toLocalDate().equals(bookingEnd.toLocalDate());
    }


    @AssertTrue(message = "Бронирование возможно только в рабочие дни")
    private boolean isWorkingDay() {
        return bookingStart.toLocalDate().getDayOfWeek() != DayOfWeek.SUNDAY
                && bookingStart.toLocalDate().getDayOfWeek() != DayOfWeek.SATURDAY;
    }
}