package com.example.social_network01.service.booking;

import com.example.social_network01.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {


    BookingDTO createBooking(BookingDTO bookingDTO, Long userId);

    Page<BookingDTO> getAllBookings(Pageable pageable);

    BookingDTO getBookingById(Long id);

    void deleteBooking(Long id, Long userId);

    List<LocalDateTime> getAvailableSlots(Long workspaceId, LocalDateTime date);

}
