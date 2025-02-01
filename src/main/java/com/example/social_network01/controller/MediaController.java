package com.example.social_network01.controller;

import com.example.social_network01.model.Media;
import com.example.social_network01.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaRepository mediaRepository;

    @GetMapping
    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable Long id) {
        return mediaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Media createMedia(@RequestBody Media media) {
        return mediaRepository.save(media);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Media> updateMedia(@PathVariable Long id, @RequestBody Media mediaDetails) {
        return mediaRepository.findById(id)
                .map(media -> {
                    media.setMediaType(mediaDetails.getMediaType());
                    media.setFileName(mediaDetails.getFileName());
                    media.setFilePath(mediaDetails.getFilePath());
                    return ResponseEntity.ok(mediaRepository.save(media));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        return mediaRepository.findById(id)
                .map(media -> {
                    mediaRepository.delete(media);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
