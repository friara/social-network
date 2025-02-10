package com.example.social_network01.service.chat_members;

import com.example.social_network01.dto.ChatMemberDTO;

import java.util.List;

public interface ChatMemberService {
    ChatMemberDTO addChatMember(ChatMemberDTO chatMemberDTO);
    List<ChatMemberDTO> getAllChatMembers();
    ChatMemberDTO getChatMemberById(Long id);
    void deleteChatMember(Long id);
}

