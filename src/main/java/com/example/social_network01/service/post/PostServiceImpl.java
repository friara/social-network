package com.example.social_network01.service.post;

import com.example.social_network01.dto.CommentDTO;
import com.example.social_network01.dto.MediaDTO;
import com.example.social_network01.dto.PostDTO;
import com.example.social_network01.exception.custom.PostNotFoundException;
import com.example.social_network01.model.Comment;
import com.example.social_network01.model.Media;
import com.example.social_network01.model.Post;
import com.example.social_network01.repository.PostRepository;
import com.example.social_network01.service.media.MediaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PostDTO createPost(String title, String text, List<MultipartFile> files) {
        Post post = new Post();
        post.setTitle(title);
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

        post.setTitle(postDTO.getTitle());
        post.setText(postDTO.getText());
        postRepository.save(post);

        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO) {
        return null;
    }
    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PostDTO mapToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setCreatedWhen(post.getCreatedWhen());
        postDTO.setTitle(post.getTitle());
        postDTO.setText(post.getText());

        // Маппим Media в MediaDTO
        postDTO.setMediaUrls(post.getMedia().stream()
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
}
