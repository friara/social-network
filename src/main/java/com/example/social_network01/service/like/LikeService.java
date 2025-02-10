package com.example.social_network01.service.like;

import com.example.social_network01.dto.LikeDTO;

import java.util.List;

public interface LikeService {
    LikeDTO createLike(LikeDTO likeDTO);
    List<LikeDTO> getAllLikes();
    LikeDTO getLikeById(Long id);
    void deleteLike(Long id);
}

