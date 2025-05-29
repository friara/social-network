package com.example.social_network01.controller;

import com.example.social_network01.dto.LikeDTO;
import com.example.social_network01.model.User;
import com.example.social_network01.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LikeDTO createLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ) {
        return likeService.createLike(postId, user.getId());
    }

    @GetMapping
    public Page<LikeDTO> getLikesByPost(
            @PathVariable Long postId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return likeService.getLikesByPost(postId, pageable);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal User currentUser
    ) {
        likeService.deleteLike(postId, currentUser.getId());
    }

    @GetMapping("/count")
    public Long getLikesCount(@PathVariable Long postId) {
        return likeService.getLikesCount(postId);
    }
}