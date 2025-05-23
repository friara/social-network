package com.example.social_network01.service.booking;

import com.example.social_network01.dto.booking.BookingDTO;
import com.example.social_network01.dto.booking.BookingRequestDTO;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.exception.custom.ConflictException;
import com.example.social_network01.model.Booking;
import com.example.social_network01.model.User;
import com.example.social_network01.model.Workspace;
import com.example.social_network01.repository.BookingRepository;
import com.example.social_network01.repository.UserRepository;
import com.example.social_network01.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingRequestDTO bookingRequestDTO, User user) {
        // Получаем связанные сущности
        Workspace workspace = workspaceRepository.findById(bookingRequestDTO.getWorkspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));

        // Проверка временных конфликтов
        checkBookingConflict(
                workspace.getId(),
                bookingRequestDTO.getBookingStart(),
                bookingRequestDTO.getBookingEnd()
        );

        // Маппинг DTO -> Entity
        Booking booking = modelMapper.map(bookingRequestDTO, Booking.class);
        booking.setWorkspace(workspace);
        booking.setUser(user);
        booking.setCreatedWhen(Instant.now());


        // Сохранение и возврат результата
        Booking savedBooking = bookingRepository.save(booking);

        Hibernate.initialize(savedBooking.getUser());
        Hibernate.initialize(savedBooking.getWorkspace());
        return modelMapper.map(savedBooking, BookingDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDTO> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(booking -> modelMapper.map(booking, BookingDTO.class));
    }

    @Override
    public Page<BookingDTO> getUserBookings(User user, Pageable pageable) {
        return bookingRepository.findAllByUser(user, pageable)
                .map(booking -> modelMapper.map(booking, BookingDTO.class));
    }

    @Override
    public Page<BookingDTO> getBookingsFromToday(User user, Pageable pageable) {
        Instant startOfDay = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        return bookingRepository.findBookingsFromTodayByUser(user, startOfDay, pageable)
                .map(booking -> {
                    BookingDTO dto = modelMapper.map(booking, BookingDTO.class);
//                    // Явно устанавливаем временную зону
//                    dto.setBookingStart(booking.getBookingStart().atZone(ZoneOffset.UTC));
//                    dto.setBookingEnd(booking.getBookingEnd().atZone(ZoneOffset.UTC));
                    return dto;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDTO getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    @Transactional
    public void deleteBooking(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new SecurityException("User is not authorized to delete this booking");
        }

        bookingRepository.delete(booking);
    }


    private void checkBookingConflict(Long workspaceId, Instant start, Instant end) {
        boolean hasConflict = bookingRepository.existsByWorkspaceIdAndTimeRange(
                workspaceId, start, end);

        if (hasConflict) {
            throw new ConflictException("Workspace is already booked for selected time");
        }
    }
}
