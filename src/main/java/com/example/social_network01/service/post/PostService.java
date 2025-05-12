package com.example.social_network01.service.post;

import com.example.social_network01.dto.PostDTO;
import com.example.social_network01.dto.PostResponseDTO;
import com.example.social_network01.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    PostDTO createPost(String text, List<MultipartFile> files);
    PostDTO updatePost(PostDTO postDTO, List<MultipartFile> files);
    PostDTO updatePost(PostDTO postDTO);
    List<PostResponseDTO> getAllPosts();
    PostDTO getPostById(Long id);
    void deletePost(Long id);
    Page<PostResponseDTO> getPostsSortedByDate(Pageable pageable, User currentUser);
    Page<PostResponseDTO> getPostsSortedByPopularity(Pageable pageable, User currentUser);
}

