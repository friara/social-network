package com.example.social_network01.controller;

import com.example.social_network01.dto.UserExtendedDTO;
import com.example.social_network01.dto.UserDTO;
import com.example.social_network01.model.User;
import com.example.social_network01.service.media.AvatarService;
import com.example.social_network01.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AvatarService avatarService;

    // Только для администраторов
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Получить текущего пользователя
    @GetMapping("/me")
    public UserDTO getCurrentUser(@AuthenticationPrincipal User user) {
        return userService.getUserById(user.getId());
    }

    // Обновить текущего пользователя
    @PutMapping("/me")
    public UserDTO updateCurrentUser(
            @AuthenticationPrincipal User user,
            @RequestBody UserDTO userDTO
    ) {
        return userService.updateUser(user.getId(), userDTO);
    }


    // Загрузка аватара для текущего пользователя
    @PostMapping("/me/avatar")
    public String uploadAvatar(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String imagePath = avatarService.saveImage(file);
        UserDTO userDTO = userService.getUserById(user.getId());
        userDTO.setAvatarUrl(imagePath);
        userService.updateUser(user.getId(), userDTO);
        return imagePath;
    }


    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> adminCreateUser(@RequestBody @Valid UserExtendedDTO request) {
        UserDTO response = userService.createUser(request);
        return ResponseEntity.ok(response); // Пароль не будет включен в ответ
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> adminUpdateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserExtendedDTO userDTO
    ) {
        UserDTO updatedUser = userService.adminUpdateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void adminDeleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/search")
    public Page<UserDTO> searchByFIO(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.searchByFIO(query, PageRequest.of(page, size));
    }

}