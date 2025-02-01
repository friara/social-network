package com.example.social_network01.controller;

import com.example.social_network01.model.Repost;
import com.example.social_network01.repository.RepostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reposts")
public class RepostController {

    @Autowired
    private RepostRepository repostRepository;

    @GetMapping
    public List<Repost> getAllReposts() {
        return repostRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repost> getRepostById(@PathVariable Long id) {
        return repostRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Repost createRepost(@RequestBody Repost repost) {
        return repostRepository.save(repost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepost(@PathVariable Long id) {
        return repostRepository.findById(id)
                .map(repost -> {
                    repostRepository.delete(repost);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
