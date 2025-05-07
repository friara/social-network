package com.example.social_network01.repository;

import com.example.social_network01.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    List<Workspace> findByIsAvailableTrue();

}
