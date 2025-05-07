package com.example.social_network01.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Schema(hidden = true)
@Entity
@Data
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isAvailable;

    @OneToMany(mappedBy = "workspace")
    private List<Booking> bookings;

    
}
