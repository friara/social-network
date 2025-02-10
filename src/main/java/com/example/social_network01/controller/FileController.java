package com.example.social_network01.controller;

import com.example.social_network01.dto.FileDTO;
import com.example.social_network01.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    public FileDTO createFile(@RequestBody FileDTO fileDTO) {
        return fileService.createFile(fileDTO);
    }

    @GetMapping
    public List<FileDTO> getAllFiles() {
        return fileService.getAllFiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDTO> getFileById(@PathVariable Long id) {
        FileDTO fileDTO = fileService.getFileById(id);
        if (fileDTO != null) {
            return ResponseEntity.ok(fileDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}
