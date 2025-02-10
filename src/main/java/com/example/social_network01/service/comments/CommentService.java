package com.example.social_network01.service.comments;

import com.example.social_network01.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO);
    List<CommentDTO> getAllComments();
    CommentDTO getCommentById(Long id);
    void deleteComment(Long id);
}

