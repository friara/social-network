package com.example.social_network01.service.chat;

import com.example.social_network01.dto.ChatDTO;
import com.example.social_network01.model.Chat;
import com.example.social_network01.repository.ChatRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChatDTO createChat(ChatDTO chatDTO) {
        Chat chat = modelMapper.map(chatDTO, Chat.class);
        return modelMapper.map(chatRepository.save(chat), ChatDTO.class);
    }

    @Override
    public List<ChatDTO> getAllChats() {
        return chatRepository.findAll().stream()
                .map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChatDTO getChatById(Long id) {
        return chatRepository.findById(id)
                .map(chat -> modelMapper.map(chat, ChatDTO.class))
                .orElse(null);
    }

    @Override
    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }
}

