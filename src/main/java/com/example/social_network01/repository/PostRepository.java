package com.example.social_network01.repository;

import com.example.social_network01.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Поиск по тексту поста
    List<Post> findByTextContaining(String text);

    // Поиск по заголовку или тексту (регистронезависимый)
    @Query(value = "SELECT p FROM Post p WHERE LOWER(CAST(p.text AS string)) LIKE LOWER(CONCAT('%', :content, '%'))", nativeQuery = true)
    List<Post> searchByContent(@Param("content") String content);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.likes " +
            "LEFT JOIN FETCH p.comments " +
            "LEFT JOIN FETCH p.reposts " +
            "LEFT JOIN FETCH p.media")
    List<Post> findAllWithAssociations();

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.likes " +
            "LEFT JOIN FETCH p.comments " +
            "LEFT JOIN FETCH p.reposts " +
            "LEFT JOIN FETCH p.media " +
            "WHERE p.id = :id")
    Optional<Post> findByIdWithAssociations(Long id);
}
