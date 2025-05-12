package com.example.social_network01.service.post;

import com.example.social_network01.dto.PostDTO;
import com.example.social_network01.dto.PostResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    public PostDTO createPost(String text, List<MultipartFile> files);
    public PostDTO updatePost(PostDTO postDTO, List<MultipartFile> files);
    public PostDTO updatePost(PostDTO postDTO);
    List<PostResponseDTO> getAllPosts();
    PostDTO getPostById(Long id);
    void deletePost(Long id);
}

