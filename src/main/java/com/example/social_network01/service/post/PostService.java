package com.example.social_network01.service.post;

import com.example.social_network01.dto.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    List<PostDTO> getAllPosts();
    PostDTO getPostById(Long id);
    void deletePost(Long id);
}

