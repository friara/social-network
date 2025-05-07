package com.example.social_network01.repository;

import com.example.social_network01.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    Long countByPostId(Long postId);
}
