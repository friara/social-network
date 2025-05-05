package com.example.social_network01.controller;


import com.example.social_network01.dto.RepostDTO;
import com.example.social_network01.model.User;
import com.example.social_network01.service.repost.RepostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reposts")
public class RepostController {

    @Autowired
    private RepostService repostService;

    @PostMapping
    public RepostDTO createRepost(@AuthenticationPrincipal User currentUser,
                                  @RequestBody RepostDTO repostDTO) {
        repostDTO.setUserId(currentUser.getId());
        return repostService.createRepost(repostDTO);
    }

    @GetMapping
    public List<RepostDTO> getAllReposts() {
        return repostService.getAllReposts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepostDTO> getRepostById(@PathVariable Long id) {
        RepostDTO repostDTO = repostService.getRepostById(id);
        if (repostDTO != null) {
            return ResponseEntity.ok(repostDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepost(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {

        RepostDTO repost = repostService.getRepostById(id);

        // Проверяем, что пользователь - автор сообщения
        if (!repost.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can't edit this repost");
        }

        repostService.deleteRepost(id);
        return ResponseEntity.noContent().build();

    }
}
