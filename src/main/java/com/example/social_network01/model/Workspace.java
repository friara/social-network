package com.example.social_network01.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String description;
    @Column(nullable = false)
    private boolean isAvailable;

    @OneToMany(mappedBy = "workspace")
    private List<Booking> bookings;

    
}
