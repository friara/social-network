package com.example.social_network01.service.like;

import com.example.social_network01.dto.LikeDTO;
import com.example.social_network01.exception.custom.DuplicateLikeException;
import com.example.social_network01.exception.custom.PostNotFoundException;
import com.example.social_network01.exception.custom.UserNotFoundException;
import com.example.social_network01.exception.custom.LikeNotFoundException;
import com.example.social_network01.model.Like;
import com.example.social_network01.model.Post;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.LikeRepository;
import com.example.social_network01.repository.PostRepository;
import com.example.social_network01.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public LikeDTO createLike(Long postId, Long userId) {
        if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new DuplicateLikeException("Like already exists");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        like.setCreatedAt(LocalDateTime.now());

        Like savedLike = likeRepository.save(like);
        return modelMapper.map(savedLike, LikeDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikeDTO> getLikesByPost(Long postId, Pageable pageable) {
        return likeRepository.findByPostId(postId, pageable)
                .map(like -> modelMapper.map(like, LikeDTO.class));
    }

    @Override
    @Transactional
    public void deleteLike(Long postId, Long userId) {
//        if (!likeRepository.existsByPostIdAndUserId(postId, userId)) {
//            throw new LikeNotFoundException("Like not found");
//        }
//        likeRepository.deleteByPostIdAndUserId(postId, userId);
        if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
            likeRepository.deleteByPostIdAndUserId(postId, userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getLikesCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}


