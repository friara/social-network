package com.example.social_network01.service.user;

import com.example.social_network01.dto.UserExtendedDTO;
import com.example.social_network01.dto.UserDTO;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.exception.custom.UserNotFoundException;
import com.example.social_network01.model.Role;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.RoleRepository;
import com.example.social_network01.repository.UserRepository;
import com.example.social_network01.service.media.AvatarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDTO createUser(UserExtendedDTO userDTO) {
        String password = userDTO.getPassword();
        // Маппинг DTO в сущность User
        User user = modelMapper.map(userDTO, User.class);

        user.setPassword(passwordEncoder.encode(password)); // Шифрование

        // Сохранение и возврат DTO
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserExtendedDTO> getAllUsersWithPassword() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserExtendedDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserExtendedDTO getUserWithPasswordById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserExtendedDTO.class))
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDTO getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new UserNotFoundException("User not found with login: " + login));
    }

    @Override
    public Page<UserDTO> searchByFIO(String query, Pageable pageable) {
        return userRepository.findByFIOContaining(query, pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        modelMapper.map(userDTO, user);
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserDTO adminUpdateUser(Long id, UserExtendedDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Обновление роли
        if (userDTO.getRoleName() != null && !userDTO.getRoleName().isEmpty()) {
            Role newRole = roleRepository.findByName(userDTO.getRoleName())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found " + userDTO.getRoleName()));
            user.setRole(newRole);
        }

        modelMapper.map(userDTO, user);

        // Игнорируем поля, которые не должны обновляться через DTO
        modelMapper.getConfiguration().setPropertyCondition(context ->
                !context.getMapping().getLastDestinationProperty().getName().equals("password")
        );

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        dto.setRoleName(user.getRole().getName());
        return dto;
    }
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO uploadAvatar(Long id, MultipartFile file) {
        String imagePath = avatarService.saveImage(file);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        Path path = Paths.get(imagePath);
        String fileName = path.getFileName().toString();
        user.setAvatarPath(fileName);
        userRepository.save(user);

        return getUserById(id);
    }

}
