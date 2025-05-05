package com.example.social_network01.controller;

import com.example.social_network01.service.StorageService;
import com.example.social_network01.service.media.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/media")
public class MediaController {


    @Autowired
    private MediaService mediaService;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> downloadMedia(@PathVariable String filename) throws IOException {
        Resource file = mediaService.getMediaFile(filename);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\""
                )
                .body(file);
    }
}
