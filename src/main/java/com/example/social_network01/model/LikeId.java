package com.example.social_network01.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Schema(hidden = true)
@Embeddable
public class LikeId implements Serializable {
    private Long user;
    private Long post;
}
