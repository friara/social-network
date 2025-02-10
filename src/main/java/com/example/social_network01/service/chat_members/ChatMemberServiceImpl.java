package com.example.social_network01.service.chat_members;

import com.example.social_network01.dto.ChatMemberDTO;
import com.example.social_network01.model.ChatMember;
import com.example.social_network01.repository.ChatMemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMemberServiceImpl implements ChatMemberService {

    @Autowired
    private ChatMemberRepository chatMemberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChatMemberDTO addChatMember(ChatMemberDTO chatMemberDTO) {
        ChatMember chatMember = modelMapper.map(chatMemberDTO, ChatMember.class);
        return modelMapper.map(chatMemberRepository.save(chatMember), ChatMemberDTO.class);
    }

    @Override
    public List<ChatMemberDTO> getAllChatMembers() {
        return chatMemberRepository.findAll().stream()
                .map(chatMember -> modelMapper.map(chatMember, ChatMemberDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChatMemberDTO getChatMemberById(Long id) {
        return chatMemberRepository.findById(id)
                .map(chatMember -> modelMapper.map(chatMember, ChatMemberDTO.class))
                .orElse(null);
    }

    @Override
    public void deleteChatMember(Long id) {
        chatMemberRepository.deleteById(id);
    }
}

