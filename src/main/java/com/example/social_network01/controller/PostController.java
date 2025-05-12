package com.example.social_network01.controller;

import com.example.social_network01.dto.PostDTO;
import com.example.social_network01.dto.PostResponseDTO;
import com.example.social_network01.model.User;
import com.example.social_network01.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostDTO createPost(
            @RequestParam("text") String text,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        return postService.createPost(text, files);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostDTO updatePost(
            @PathVariable("id") Long id,
            @ModelAttribute PostDTO postDTO,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) {
        postDTO.setId(id);
        return postService.updatePost(postDTO, files);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAllPosts(
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdWhen").descending());

        return switch (sortBy.toLowerCase()) {
            case "popularity" ->
                    ResponseEntity.ok(postService.getPostsSortedByPopularity(pageable, currentUser));
            default ->
                    ResponseEntity.ok(postService.getPostsSortedByDate(pageable, currentUser));
        };
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

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
