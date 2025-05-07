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

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(
            @PathVariable Long postId,
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser
    ) {
        if (!userId.equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own likes");
        }
        likeService.deleteLike(postId, userId);
    }

    @GetMapping("/count")
    public Long getLikesCount(@PathVariable Long postId) {
        return likeService.getLikesCount(postId);
    }
}


//@RestController
//@RequestMapping("/api/likes")
//public class LikeController {
//
//    @Autowired
//    private LikeService likeService;
//
//    @PostMapping
//    public LikeDTO createLike(@AuthenticationPrincipal User currentUser,
//                              @RequestBody LikeDTO likeDTO) {
//        likeDTO.setUserId(currentUser.getId());
//        return likeService.createLike(likeDTO);
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping
//    public List<LikeDTO> getAllLikes() {
//        return likeService.getAllLikes();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<LikeDTO> getLikeById(@PathVariable Long id) {
//        LikeDTO likeDTO = likeService.getLikeById(id);
//        if (likeDTO != null) {
//            return ResponseEntity.ok(likeDTO);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteLike(@AuthenticationPrincipal User currentUser,
//                                           @PathVariable Long id) {
//        likeService.deleteLike(id);
//        return ResponseEntity.noContent().build();
//    }
//}
