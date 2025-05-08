package com.example.social_network01.repository;

import com.example.social_network01.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // Метод для поиска по названию роли
    Optional<Role> findByName(String name);

    @Query("SELECT r FROM Role r WHERE LOWER(r.name) = LOWER(:name)")
    Optional<Role> findByNameCaseInsensitive(@Param("name") String name);
}

