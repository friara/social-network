package com.example.social_network01.controller;

import com.example.social_network01.dto.UserDTO;
import com.example.social_network01.service.media.ImageService;
import com.example.social_network01.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ImageService imageService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * Загрузка аватара пользователя.
     */
    @PostMapping("/{id}/avatar")
    public String uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        // Сохранение изображения на сервере
        String imagePath = imageService.saveImage(file);

        // Обновление информации о пользователе в базе данных
        UserDTO userDTO = userService.getUserById(id);
        userDTO.setAvatarUrl(imagePath);
        userService.updateUser(id, userDTO);

        return imagePath;
    }

}
