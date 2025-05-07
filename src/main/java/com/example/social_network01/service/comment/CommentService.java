package com.example.social_network01.service.comment;

import com.example.social_network01.dto.CommentDTO;
import com.example.social_network01.model.User;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO);
    List<CommentDTO> getAllComments();
    CommentDTO getCommentById(Long id);
    void deleteComment(Long id);
    List<CommentDTO> getCommentsByPostId(Long postId);
    CommentDTO createComment(Long postId, CommentDTO request, User user);
    Long getCommentCountByPostId(Long postId);
    CommentDTO updateComment(Long commentId, CommentDTO commentDTO, User currentUser);
    void deleteComment(Long commentId, User currentUser);
}

