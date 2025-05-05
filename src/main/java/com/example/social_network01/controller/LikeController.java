package com.example.social_network01.controller;

import com.example.social_network01.dto.LikeDTO;
import com.example.social_network01.model.User;
import com.example.social_network01.service.like.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public LikeDTO createLike(@AuthenticationPrincipal User currentUser,
                              @RequestBody LikeDTO likeDTO) {
        likeDTO.setUserId(currentUser.getId());
        return likeService.createLike(likeDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<LikeDTO> getAllLikes() {
        return likeService.getAllLikes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LikeDTO> getLikeById(@PathVariable Long id) {
        LikeDTO likeDTO = likeService.getLikeById(id);
        if (likeDTO != null) {
            return ResponseEntity.ok(likeDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@AuthenticationPrincipal User currentUser,
                                           @PathVariable Long id) {
        likeService.deleteLike(id);
        return ResponseEntity.noContent().build();
    }
}
