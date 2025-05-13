package com.example.social_network01.repository;

import com.example.social_network01.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByMessageId(Long messageId);
    void deleteByMessageId(Long messageId);
}

