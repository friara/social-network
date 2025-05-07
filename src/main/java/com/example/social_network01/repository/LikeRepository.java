package com.example.social_network01.repository;

import com.example.social_network01.model.Like;
import com.example.social_network01.model.LikeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    List<Like> findByPostId(Long postId);
    Page<Like> findByPostId(Long postId, Pageable pageable);
    boolean deleteByPostIdAndUserId(Long postId, Long userId);
    Long countByPostId(Long postId);
}
