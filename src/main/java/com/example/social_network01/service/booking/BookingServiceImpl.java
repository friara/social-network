package com.example.social_network01.service.booking;

import com.example.social_network01.dto.BookingDTO;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.exception.custom.ConflictException;
import com.example.social_network01.model.Booking;
import com.example.social_network01.model.User;
import com.example.social_network01.model.Workspace;
import com.example.social_network01.repository.BookingRepository;
import com.example.social_network01.repository.UserRepository;
import com.example.social_network01.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO, Long userId) {
        Workspace workspace = workspaceRepository.findById(bookingDTO.getWorkspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        checkBookingConflict(workspace.getId(), bookingDTO.getBookingStart(), bookingDTO.getBookingEnd());

        Booking booking = modelMapper.map(bookingDTO, Booking.class);
        booking.setWorkspace(workspace);
        booking.setUser(user);
        booking.setCreatedWhen(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);
        return modelMapper.map(savedBooking, BookingDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDTO> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(booking -> modelMapper.map(booking, BookingDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDTO getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public void deleteBooking(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new SecurityException("User is not authorized to delete this booking");
        }

        bookingRepository.delete(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDateTime> getAvailableSlots(Long workspaceId, LocalDateTime date) {
        // Реализация проверки доступных слотов времени
        // (может быть сложной логикой в зависимости от требований)
        return List.of();
    }

    private void checkBookingConflict(Long workspaceId, LocalDateTime start, LocalDateTime end) {
        boolean hasConflict = bookingRepository.existsByWorkspaceIdAndTimeRange(
                workspaceId, start, end);

        if (hasConflict) {
            throw new ConflictException("Workspace is already booked for selected time");
        }
    }
}
