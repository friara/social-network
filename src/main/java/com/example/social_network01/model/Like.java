package com.example.social_network01.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(hidden = true)
@Entity
@Data
@Table(name = "Likes")
@IdClass(LikeId.class) // Для составного ключа (user + post)
public class Like {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

