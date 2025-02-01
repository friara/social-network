package com.example.social_network01.repository;

import com.example.social_network01.model.MessageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageFileRepository extends JpaRepository<MessageFile, Long> {
}
