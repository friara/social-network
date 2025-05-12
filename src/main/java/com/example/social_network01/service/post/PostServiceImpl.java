package com.example.social_network01.service.post;

import com.example.social_network01.dto.MediaDTO;
import com.example.social_network01.dto.PostDTO;
import com.example.social_network01.dto.PostResponseDTO;
import com.example.social_network01.exception.custom.PostNotFoundException;
import com.example.social_network01.model.Media;
import com.example.social_network01.model.Post;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.PostRepository;
import com.example.social_network01.service.media.MediaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MediaService mediaService;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        return modelMapper.map(postRepository.save(post), PostDTO.class);
    }

    // Создание нового поста с прикрепленными медиафайлами.
    @Override
    @Transactional
    public PostDTO createPost(String text, List<MultipartFile> files) {
        Post post = new Post();
        post.setText(text);
        post.setCreatedWhen(LocalDateTime.now());
        post = postRepository.save(post);

        List<Media> mediaList = (files != null && !files.isEmpty())
                ? mediaService.saveMediaFiles(files, post)
                : Collections.emptyList();
        post.setMedia(mediaList);

        return mapToDTO(post);
    }

    @Override
    @Transactional
    public PostDTO updatePost(PostDTO postDTO, List<MultipartFile> files) {
        Post post = postRepository.findById(postDTO.getId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        if (post.getMedia() != null && !post.getMedia().isEmpty()) {
            mediaService.deleteMedia(post.getMedia());
        }

        List<Media> mediaList = (files != null && !files.isEmpty())
                ? mediaService.saveMediaFiles(files, post)
                : Collections.emptyList();
        post.setMedia(mediaList);

        post.setText(postDTO.getText());
        postRepository.save(post);

        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO) {
        return null;
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PostResponseDTO.class))
//                        .setMedia(p.getMedia().stream()
//                        .map(media -> modelMapper.map(media, MediaDTO.class))
//                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    private PostDTO mapToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setCreatedWhen(post.getCreatedWhen());
        postDTO.setText(post.getText());

        // Маппим Media в MediaDTO
        postDTO.setMedia(post.getMedia().stream()
                .map(media -> modelMapper.map(media, MediaDTO.class)) // Используем ModelMapper
                .collect(Collectors.toList()));

        return postDTO;
    }


    @Override
    public PostDTO getPostById(Long id) {
        return postRepository.findById(id)
                .map(post -> modelMapper.map(post, PostDTO.class))
                .orElse(null);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    private boolean isPostLikedByCurrentUser(Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String username = authentication.getName();
        return post.getLikes().stream()
                .anyMatch(like -> like.getUser().getLogin().equals(username));
    }

    @Override
    public Page<PostResponseDTO> getPostsSortedByDate(Pageable pageable, User currentUser) {
        return postRepository.findAllByOrderByCreatedWhenDesc(pageable)
                .map(post -> {
                    PostResponseDTO dto = modelMapper.map(post, PostResponseDTO.class);
                    dto.setMedia(mapMedia(post.getMedia())); // Маппинг медиа
                    dto.setLiked(checkIfLiked(post, currentUser)); // Логика проверки лайка
                    return dto;
                });
    }

    private List<MediaDTO> mapMedia(List<Media> mediaList) {
        return mediaList.stream()
                .map(media -> modelMapper.map(media, MediaDTO.class))
                .toList();
    }

    private boolean checkIfLiked(Post post, User user) {
        return post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(user.getId()));
    }

    @Override
    public Page<PostResponseDTO> getPostsSortedByPopularity(Pageable pageable, User currentUser) {
        return postRepository.findAllOrderByPopularityDesc(pageable)
                .map(post -> {
                    PostResponseDTO dto = modelMapper.map(post, PostResponseDTO.class);
                    dto.setMedia(mapMedia(post.getMedia())); // Маппинг медиа
                    dto.setLiked(checkIfLiked(post, currentUser)); // Логика проверки лайка
                    return dto;
                });
    }
}
