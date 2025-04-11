package com.example.social_network01.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime bookingStart;
    @Column(nullable = false)
    private LocalDateTime bookingEnd;
    @Past
    private LocalDateTime createdWhen;
}

