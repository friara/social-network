package com.example.social_network01.service.repost;

import com.example.social_network01.dto.RepostDTO;
import com.example.social_network01.model.Repost;
import com.example.social_network01.repository.RepostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepostServiceImpl implements RepostService {

    @Autowired
    private RepostRepository repostRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RepostDTO createRepost(RepostDTO repostDTO) {
        Repost repost = modelMapper.map(repostDTO, Repost.class);
        return modelMapper.map(repostRepository.save(repost), RepostDTO.class);
    }

    @Override
    public List<RepostDTO> getAllReposts() {
        return repostRepository.findAll().stream()
                .map(repost -> modelMapper.map(repost, RepostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RepostDTO getRepostById(Long id) {
        return repostRepository.findById(id)
                .map(repost -> modelMapper.map(repost, RepostDTO.class))
                .orElse(null);
    }

    @Override
    public void deleteRepost(Long id) {
        repostRepository.deleteById(id);
    }

    @Override
    public List<RepostDTO> getRepostByPostId(Long postId) {
        return repostRepository.findAllByPost_Id(postId)
                .stream().map(repost -> modelMapper.map(repost, RepostDTO.class))
                .collect(Collectors.toList());
    }
}
