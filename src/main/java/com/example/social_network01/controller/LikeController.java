package com.example.social_network01.controller;

import com.example.social_network01.dto.LikeDTO;
import com.example.social_network01.service.like.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public LikeDTO createLike(@RequestBody LikeDTO likeDTO) {
        return likeService.createLike(likeDTO);
    }

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
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        likeService.deleteLike(id);
        return ResponseEntity.noContent().build();
    }
}
