package com.example.social_network01.controller;

import com.example.social_network01.dto.PostDTO;
import com.example.social_network01.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public PostDTO createPost(
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam("files") List<MultipartFile> files) {
        return postService.createPost(title, text, files);
    }
    @PutMapping("/{id}")
    public PostDTO updatePost(
            @RequestBody PostDTO postDTO,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        return postService.updatePost(postDTO, files);
    }

    @GetMapping
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        PostDTO postDTO = postService.getPostById(id);
        if (postDTO != null) {
            return ResponseEntity.ok(postDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
