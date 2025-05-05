package com.example.social_network01.controller;

import com.example.social_network01.dto.UserDTO;
import com.example.social_network01.model.User;
import com.example.social_network01.service.media.AvatarService;
import com.example.social_network01.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    public UserDTO adminCreateUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
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



//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AvatarService avatarService;
//
//    @PostMapping
//    public UserDTO createUser(@RequestBody UserDTO userDTO) {
//        return userService.createUser(userDTO);
//    }
//
//    @GetMapping
//    public List<UserDTO> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//
//    // Поиск по ID
//    @GetMapping("/{id}")
//    public UserDTO getUserById(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }
//
//    // Для поиска по логину
//    @GetMapping("/by-login/{login}")
//    public UserDTO getUserByLogin(@PathVariable String login) {
//        return userService.getUserByLogin(login);
//    }
//
//    @PutMapping("/{id}")
//    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
//        return userService.updateUser(id, userDTO);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//    }
//
//    /**
//     * Загрузка аватара пользователя.
//     */
//    @PostMapping("/{id}/avatar")
//    public String uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
//        // Сохранение изображения на сервере
//        String imagePath = avatarService.saveImage(file);
//
//        // Обновление информации о пользователе в базе данных
//        UserDTO userDTO = userService.getUserById(id);
//        userDTO.setAvatarUrl(imagePath);
//        userService.updateUser(id, userDTO);
//
//        return imagePath;
//    }
//
//    @GetMapping("/search")
//    public Page<UserDTO> searchByFIO(
//            @RequestParam String query,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        return userService.searchByFIO(query, PageRequest.of(page, size));
//    }
//
//}
