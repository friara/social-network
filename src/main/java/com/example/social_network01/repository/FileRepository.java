package com.example.social_network01.repository;

import com.example.social_network01.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}

