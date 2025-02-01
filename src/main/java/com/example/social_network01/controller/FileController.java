package com.example.social_network01.controller;

import com.example.social_network01.model.File;
import com.example.social_network01.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @GetMapping
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id) {
        return fileRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public File createFile(@RequestBody File file) {
        return fileRepository.save(file);
    }

    @PutMapping("/{id}")
    public ResponseEntity<File> updateFile(@PathVariable Long id, @RequestBody File fileDetails) {
        return fileRepository.findById(id)
                .map(file -> {
                    file.setFileName(fileDetails.getFileName());
                    file.setFilePath(fileDetails.getFilePath());
                    file.setFileType(fileDetails.getFileType());
                    return ResponseEntity.ok(fileRepository.save(file));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        return fileRepository.findById(id)
                .map(file -> {
                    fileRepository.delete(file);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

