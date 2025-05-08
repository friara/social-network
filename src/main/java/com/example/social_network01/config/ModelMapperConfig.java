package com.example.social_network01.config;

import com.example.social_network01.dto.*;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.model.*;
import com.example.social_network01.repository.RoleRepository;
import com.example.social_network01.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {



    @Bean
    public ModelMapper modelMapper(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        ModelMapper modelMapper = new ModelMapper();

        // Маппинг для Media -> MediaDTO
        modelMapper.typeMap(Media.class, MediaDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getPost().getId(), MediaDTO::setPostId);
                    mapper.using(ctx -> convertToUrl((String) ctx.getSource(), "media"))
                            .map(Media::getFileName, MediaDTO::setDownloadUrl); // Используем fileName
                });

        // User -> UserDTO
        modelMapper.typeMap(User.class, UserDTO.class)
                .addMappings(mapper -> {
                    mapper.using(ctx -> convertToUrl((String) ctx.getSource(), "avatars"))
                            .map(User::getAvatarPath, UserDTO::setAvatarUrl);

                    // Маппинг роли
                    mapper.map(src -> src.getRole().getName(), UserDTO::setRoleName);

                });

        // UserDTO -> User (для обновления)
        modelMapper.typeMap(UserDTO.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setId);
                    mapper.skip(User::setPassword);
                    mapper.skip(User::setRole);

                    // Кастомный маппинг для роли
                    mapper.using(ctx -> {
                        String roleName = (String) ctx.getSource();
                        return roleRepository.findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                    }).map(UserDTO::getRoleName, User::setRole);
                });

        // Маппинг для UserExtendedDTO
        modelMapper.typeMap(UserExtendedDTO.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setId);
                    mapper.using(ctx -> passwordEncoder.encode((String) ctx.getSource()))
                            .map(UserExtendedDTO::getPassword, User::setPassword);

                    mapper.using(ctx -> {
                        String roleName = (String) ctx.getSource();
                        return roleRepository.findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                    }).map(UserExtendedDTO::getRoleName, User::setRole);
                });

        // Маппинг для File -> FileDTO (fileUrl)
        modelMapper.typeMap(File.class, FileDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getMessage().getId(), FileDTO::setMessageId);
                    mapper.using(ctx -> convertToUrl((String) ctx.getSource(), "files"))
                            .map(File::getFileName, FileDTO::setFileUrl);
                });

        // Маппинг для Message
        modelMapper.typeMap(Message.class, MessageDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUser().getId(), MessageDTO::setUserId);
                    mapper.map(src -> src.getChat().getId(), MessageDTO::setChatId);
                });

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        // Настройка маппинга для Chat
        modelMapper.typeMap(Chat.class, ChatDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getCreatedBy().getId(), ChatDTO::setCreatedBy);
            mapper.using(ctx -> ((List<ChatMember>) ctx.getSource()).stream()
                            .map(cm -> cm.getUser().getId())
                            .collect(Collectors.toList()))
                    .map(Chat::getChatMembers, ChatDTO::setParticipantIds);
        });

        modelMapper.typeMap(ChatDTO.class, Chat.class).addMappings(mapper -> {
            mapper.skip(Chat::setChatMembers);
            mapper.skip(Chat::setCreatedBy);
        });

        return modelMapper;
    }


    // Универсальный метод для генерации URL
    private String convertToUrl(String filePath, String endpoint) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }

        // Извлекаем имя файла из пути
        String filename = filePath.contains("/")
                ? filePath.substring(filePath.lastIndexOf('/') + 1)
                : filePath;

        return "/api/" + endpoint + "/" + filename;
    }
}

