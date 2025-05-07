package com.example.social_network01.service.comment;

import com.example.social_network01.dto.CommentDTO;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.model.Comment;
import com.example.social_network01.model.Post;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.CommentRepository;
import com.example.social_network01.repository.PostRepository;
import com.example.social_network01.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDTO createComment(Long postId, CommentDTO request, User currentUser) {
        // Находим пост
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));


        // Создаем сущность комментария
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setPost(post);
        comment.setUser(currentUser);

        // Сохраняем и возвращаем DTO
        return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
    }

    @Override
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long id) {
        return commentRepository.findById(id)
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .orElse(null);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long getCommentCountByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
    @Override
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can't edit this comment");
        }

        comment.setText(commentDTO.getText());
        return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
    }

    @Override
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));


        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can't delete this comment");
        }

        commentRepository.delete(comment);
    }
}
