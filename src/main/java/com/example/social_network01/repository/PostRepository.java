package com.example.social_network01.repository;

import com.example.social_network01.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Поиск по тексту поста
    List<Post> findByTextContaining(String text);

    // Поиск по заголовку или тексту (регистронезависимый)
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.text) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Post> searchByContent(@Param("keyword") String keyword);
}
