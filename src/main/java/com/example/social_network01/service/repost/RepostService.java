package com.example.social_network01.service.repost;

import com.example.social_network01.dto.RepostDTO;

import java.util.List;

public interface RepostService {
    RepostDTO createRepost(RepostDTO repostDTO);
    List<RepostDTO> getAllReposts();
    RepostDTO getRepostById(Long id);
    void deleteRepost(Long id);

    List<RepostDTO> getRepostByPostId(Long postId);
}
