package com.example.social_network01.controller;

import com.example.social_network01.dto.booking.BookingDTO;
import com.example.social_network01.dto.booking.BookingRequestDTO;
import com.example.social_network01.model.User;
import com.example.social_network01.service.booking.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(
            @RequestBody @Valid BookingRequestDTO bookingDTO,
            @AuthenticationPrincipal User user) {
        BookingDTO createdBooking = bookingService.createBooking(bookingDTO, user);

        URI location = URI.create("/api/bookings/" + createdBooking.getId());
        return ResponseEntity.created(location).body(createdBooking);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<BookingDTO>> getAllBookings(Pageable pageable) {
        return ResponseEntity.ok(bookingService.getAllBookings(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        bookingService.deleteBooking(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability/{workspaceId}")
    public ResponseEntity<List<LocalDateTime>> checkAvailability(
            @PathVariable Long workspaceId,
            @RequestParam LocalDateTime date) {
        List<LocalDateTime> slots = bookingService.getAvailableSlots(workspaceId, date);
        return ResponseEntity.ok(slots);
    }
}
