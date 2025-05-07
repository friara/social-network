package com.example.social_network01.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class LikeId implements Serializable {
    private Long user;
    private Long post;
}
