package com.example.social_network01.service.user;

import com.example.social_network01.dto.UserCreateRequestDTO;
import com.example.social_network01.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserCreateRequestDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByLogin(String login);
    Page<UserDTO> searchByFIO(String query, Pageable pageable);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}


