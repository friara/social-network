package com.example.social_network01.service.user;

import com.example.social_network01.dto.UserExtendedDTO;
import com.example.social_network01.dto.UserDTO;
import com.example.social_network01.exception.custom.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public interface UserService {
    UserDTO createUser(UserExtendedDTO userDTO);
    UserDTO adminUpdateUser(Long id, UserExtendedDTO userDTO);
    public List<UserExtendedDTO> getAllUsersWithPassword();
    public UserExtendedDTO getUserWithPasswordById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByLogin(String login);
    Page<UserDTO> searchByFIO(String query, Pageable pageable);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO uploadAvatar(Long id, MultipartFile file);
}


