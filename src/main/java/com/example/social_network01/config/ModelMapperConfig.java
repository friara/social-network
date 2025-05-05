package com.example.social_network01.config;

import com.example.social_network01.dto.FileDTO;
import com.example.social_network01.dto.MediaDTO;
import com.example.social_network01.dto.MessageDTO;
import com.example.social_network01.dto.UserDTO;
import com.example.social_network01.model.File;
import com.example.social_network01.model.Media;
import com.example.social_network01.model.Message;
import com.example.social_network01.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Маппинг для Media -> MediaDTO
        modelMapper.typeMap(Media.class, MediaDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getPost().getId(), MediaDTO::setPostId);
                    mapper.using(ctx -> convertToUrl((String) ctx.getSource(), "media"))
                            .map(Media::getFileName, MediaDTO::setDownloadUrl); // Используем fileName
                });

        // Маппинг для User -> UserDTO (avatarUrl)
        modelMapper.typeMap(User.class, UserDTO.class)
                .addMappings(mapper -> {
                    mapper.using(ctx -> convertToUrl((String) ctx.getSource(), "avatars"))
                            .map(User::getAvatarPath, UserDTO::setAvatarUrl);
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

