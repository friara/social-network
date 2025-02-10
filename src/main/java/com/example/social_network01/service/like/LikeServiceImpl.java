package com.example.social_network01.service.like;

import com.example.social_network01.dto.LikeDTO;
import com.example.social_network01.model.Like;
import com.example.social_network01.repository.LikeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LikeDTO createLike(LikeDTO likeDTO) {
        Like like = modelMapper.map(likeDTO, Like.class);
        return modelMapper.map(likeRepository.save(like), LikeDTO.class);
    }

    @Override
    public List<LikeDTO> getAllLikes() {
        return likeRepository.findAll().stream()
                .map(like -> modelMapper.map(like, LikeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public LikeDTO getLikeById(Long id) {
        return likeRepository.findById(id)
                .map(like -> modelMapper.map(like, LikeDTO.class))
                .orElse(null);
    }

    @Override
    public void deleteLike(Long id) {
        likeRepository.deleteById(id);
    }
}

