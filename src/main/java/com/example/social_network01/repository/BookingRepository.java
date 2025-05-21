package com.example.social_network01.repository;

import com.example.social_network01.model.Booking;
import com.example.social_network01.model.Message;
import com.example.social_network01.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b WHERE b.workspace.id = :workspaceId " +
            "AND ((b.bookingStart < :end AND b.bookingEnd > :start))")
    boolean existsByWorkspaceIdAndTimeRange(
            @Param("workspaceId") Long workspaceId,
            @Param("start") Instant start,
            @Param("end") Instant end);

    Page<Booking> findAllByUser(User user, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.user = :user " +
            "AND b.bookingStart >= :startOfDay")
    Page<Booking> findBookingsFromTodayByUser(
            @Param("user") User user,
            @Param("startOfDay") Instant startOfDay,
            Pageable pageable
    );
}
