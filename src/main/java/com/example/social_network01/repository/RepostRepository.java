package com.example.social_network01.repository;

import com.example.social_network01.model.Repost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepostRepository extends JpaRepository<Repost, Long> {
}

