package com.example.social_network01.service.notification;

import com.example.social_network01.model.ChatMember;
import com.example.social_network01.repository.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMemberCacheHelper {
    private final ChatMemberRepository chatMemberRepository;

    @Cacheable(value = "chatMembers", key = "#chatId")
    public List<ChatMember> getCachedChatMembers(Long chatId) {
        return chatMemberRepository.findByChatId(chatId);
    }
}
