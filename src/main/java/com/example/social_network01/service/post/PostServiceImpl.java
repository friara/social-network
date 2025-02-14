package com.example.social_network01.service.post;

import com.example.social_network01.dto.CommentDTO;
import com.example.social_network01.dto.PostDTO;
import com.example.social_network01.model.Comment;
import com.example.social_network01.model.Media;
import com.example.social_network01.model.Post;
import com.example.social_network01.repository.PostRepository;
import com.example.social_network01.service.media.MediaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    /**
     * Создание нового поста с прикрепленными медиафайлами.
     */
    @Override
    public PostDTO createPost(String title, String text, List<MultipartFile> files) {

        // Создание объекта поста
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setCreatedWhen(LocalDateTime.now());

        // Сохранение медиафайлов
        List<Media> mediaList = mediaService.saveMediaFiles(files, post);

        post.setMedia(mediaList);

        // Сохранение поста
        postRepository.save(post);

        // Преобразование поста в DTO
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, List<MultipartFile> files) {
        Post post = postRepository.findById(postDTO.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(postDTO.getTitle());
        post.setText(postDTO.getText());

        // Обновление медиафайлов
        if (files != null && !files.isEmpty()) {
            List<Media> mediaList = mediaService.saveMediaFiles(files, post);
            post.setMedia(mediaList);
        }

        // Сохранение обновленного поста
        postRepository.save(post);

        // Преобразование поста в DTO
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
        postDTO.setMediaUrls(post.getMedia().stream()
                .map(Media::getFilePath)
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
