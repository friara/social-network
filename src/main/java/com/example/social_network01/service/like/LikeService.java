package com.example.social_network01.service.like;

import com.example.social_network01.dto.LikeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface LikeService {

    LikeDTO createLike(Long postId, Long userId);

    Page<LikeDTO> getLikesByPost(Long postId, Pageable pageable);

    void deleteLike(Long postId, Long userId);

    Long getLikesCount(Long postId);

}

