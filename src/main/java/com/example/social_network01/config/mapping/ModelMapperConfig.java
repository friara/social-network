package com.example.social_network01.config.mapping;

import com.example.social_network01.dto.*;
import com.example.social_network01.dto.booking.BookingDTO;
import com.example.social_network01.dto.message.MessageDTO;
import com.example.social_network01.dto.post.PostResponseDTO;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.model.*;
import com.example.social_network01.repository.CommentRepository;
import com.example.social_network01.repository.RoleRepository;
import com.example.social_network01.repository.UserRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {



    @Bean
    public ModelMapper modelMapper(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CommentRepository commentRepository,
            PasswordEncoder passwordEncoder) {
        ModelMapper modelMapper = new ModelMapper();

        // Маппинг для Media -> MediaDTO
        modelMapper.typeMap(Media.class, MediaDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getPost().getId(), MediaDTO::setPostId);
                    mapper.using(ctx -> convertToUrl((String) ctx.getSource(), "media"))
                            .map(Media::getFileName, MediaDTO::setDownloadUrl);
                });

        // UserDTO -> User
        modelMapper.emptyTypeMap(UserDTO.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setId);
                    mapper.skip(User::setPassword);
                    mapper.skip(User::setRole); // Пропускаем сначала поле role
                })
                .implicitMappings(); // Добавляем неявные маппинги для остальных полей

        // User -> UserDTO
        modelMapper.typeMap(User.class, UserDTO.class)
                .addMappings(mapper -> {
                    mapper.using(ctx -> {
                        String path = (String) ctx.getSource();
                        return (path != null) ? convertToUrl(path, "avatars") : null;
                    }).map(User::getAvatarPath, UserDTO::setAvatarUrl);

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

        // UserExtendedDTO -> User
        modelMapper.emptyTypeMap(UserExtendedDTO.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setId);
                    mapper.skip(User::setRole); // Пропускаем сначала поле role
                })
                .implicitMappings();

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

        Converter<Collection, Integer> collectionToSize = c -> (c.getSource() != null) ? c.getSource().size() : 0;
        // Маппинг для Post -> PostResponseDTO
        modelMapper.typeMap(Post.class, PostResponseDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUser().getId(), PostResponseDTO::setUserId);
                    mapper.using(collectionToSize).map(Post::getLikes, PostResponseDTO::setLikeCount);
                    mapper.using(collectionToSize).map(Post::getComments, PostResponseDTO::setCommentCount);
                    mapper.skip(PostResponseDTO::setLiked);
                    // Маппинг коллекции Media → MediaDTO
                    mapper.using(ctx ->
                            ((Collection<Media>) ctx.getSource()).stream()
                                    .map(media -> modelMapper.map(media, MediaDTO.class))
                                    .collect(Collectors.toList())
                    ).map(Post::getMedia, PostResponseDTO::setMedia);
                });


        // Маппинг для File -> FileDTO (fileUrl)
        modelMapper.typeMap(File.class, FileDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getMessage().getId(), FileDTO::setMessageId);
                    mapper.using(ctx -> convertToUrl((String) ctx.getSource(), "files"))
                            .map(File::getFileName, FileDTO::setFileUrl);
                    mapper.map(File::getOriginalFileName, FileDTO::setFileName);
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


        // Конвертер для списка бронирований (Booking -> BookingDTO с фильтрацией)
        Converter<List<Booking>, List<BookingDTO>> bookingConverter = ctx -> {
            List<Booking> bookings = ctx.getSource();
            if (bookings == null) {
                return Collections.emptyList();
            }
            // Получаем начало текущего дня в системной временной зоне и конвертируем в Instant
            Instant startOfDay = LocalDate.now()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant();

            return bookings.stream()
                    .filter(booking -> booking.getBookingStart() != null
                            && !booking.getBookingStart().isBefore(startOfDay))
                    .map(booking -> modelMapper.map(booking, BookingDTO.class))
                    .collect(Collectors.toList());
        };

        // Маппинг Workspace -> WorkspaceDTO
        modelMapper.typeMap(Workspace.class, WorkspaceDTO.class)
                .addMappings(mapper -> {
                    mapper.using(bookingConverter).map(Workspace::getBookings, WorkspaceDTO::setCurrentBookings);
                    mapper.map(src -> src.isAvailable(), WorkspaceDTO::setAvailable); // Маппинг boolean (если нужно)
                });

        modelMapper.typeMap(Booking.class, BookingDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getWorkspace().getId(), BookingDTO::setWorkspaceId);
                    mapper.map(src -> src.getUser().getId(), BookingDTO::setUserId);
                });

        // Маппинг Comment -> CommentDTO
        modelMapper.typeMap(Comment.class, CommentDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getPost().getId(), CommentDTO::setPostId);
                    mapper.map(src -> src.getUser().getId(), CommentDTO::setUserId);
                    mapper.using(ctx -> {
                        Comment parent = (Comment) ctx.getSource(); // Получаем сам answerToComm
                        return parent != null ? parent.getId() : null;
                    }).map(Comment::getAnswerToComm, CommentDTO::setAnswerToComm);
                });

        // Маппинг CommentDTO -> Comment
        modelMapper.typeMap(CommentDTO.class, Comment.class)
                .addMappings(mapper -> {
                    mapper.skip(Comment::setPost);
                    mapper.skip(Comment::setUser);
                    mapper.skip(Comment::setAnswerToComm);

                    // Кастомный маппинг для answerToComm
                    mapper.using(ctx -> {
                        Long parentId = (Long) ctx.getSource();
                        return parentId != null ?
                                commentRepository.findById(parentId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"))
                                : null;
                    }).map(CommentDTO::getAnswerToComm, Comment::setAnswerToComm);
                });


        //modelMapper.validate();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
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

