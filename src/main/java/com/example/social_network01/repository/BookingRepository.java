package com.example.social_network01.repository;

import com.example.social_network01.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b WHERE b.workspace.id = :workspaceId " +
            "AND ((b.bookingStart < :end AND b.bookingEnd > :start))")
    boolean existsByWorkspaceIdAndTimeRange(
            @Param("workspaceId") Long workspaceId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
