package com.example.social_network01.controller;

import com.example.social_network01.dto.CommentDTO;
import com.example.social_network01.model.User;
import com.example.social_network01.service.comment.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentDTO request,
            @AuthenticationPrincipal User currentUser) {

        CommentDTO response = commentService.createComment(postId, request, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<CommentDTO> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/{commentId}")
    public CommentDTO getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCommentCount(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentCountByPostId(postId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentDTO commentDTO,
            @AuthenticationPrincipal User currentUser) {

        CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO, currentUser);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal User currentUser) {

        commentService.deleteComment(commentId, currentUser);
        return ResponseEntity.noContent().build();
    }
}