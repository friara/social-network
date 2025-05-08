package com.example.social_network01.service.chat;

import com.example.social_network01.dto.ChatDTO;
import com.example.social_network01.dto.ChatSummaryDTO;
import com.example.social_network01.exception.custom.ResourceNotFoundException;
import com.example.social_network01.exception.custom.UserNotFoundException;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.ChatMember;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.ChatRepository;
import com.example.social_network01.repository.MessageRepository;
import com.example.social_network01.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

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
    @Transactional
    public ChatDTO createChat(ChatDTO chatDTO, User creator) {
        validateParticipants(chatDTO);

        Chat chat = modelMapper.map(chatDTO, Chat.class);
        chat.setCreatedBy(creator);
        chat.setCreatedWhen(LocalDateTime.now());

        addParticipants(chat, chatDTO.getParticipantIds());
        addCreatorAsMember(chat, creator);

        return convertToDTO(chatRepository.save(chat));
    }

    @Override
    @Transactional(readOnly = true)
    public ChatDTO getChatById(Long id) {
        return chatRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
    }

    @Override
    @Transactional
    public ChatDTO updateChat(Long id, ChatDTO chatDTO) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        modelMapper.map(chatDTO, chat);
        return convertToDTO(chatRepository.save(chat));
    }

    @Override
    @Transactional
    public void deleteChat(Long id) {
        chatRepository.deleteById(id);
    }

    @Override
    public boolean isChatCreator(Long chatId, Long userId) {
        return chatRepository.existsByIdAndCreatedBy_Id(chatId, userId);
    }
    @Override
    public boolean isUserParticipant(Long chatId, Long userId) {
        return chatRepository.existsByIdAndChatMembers_User_Id(chatId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatSummaryDTO> getUserChats(Long userId, String search, Pageable pageable) {
        Page<Chat> chats = chatRepository.findUserChatsWithSearch(userId, search, pageable);
        return chats.map(c -> convertToSummaryDTO(c, userId));
    }

    private ChatSummaryDTO convertToSummaryDTO(Chat chat, Long userId) {
        ChatSummaryDTO dto = modelMapper.map(chat, ChatSummaryDTO.class);

        // Получаем последнее сообщение
        messageRepository.findFirstByChatOrderByCreatedAtDesc(chat)
                .ifPresent(msg -> {
                    dto.setLastActivity(msg.getCreatedWhen());
                    dto.setLastMessagePreview(msg.getText());
                });

        // Считаем непрочитанные сообщения
        dto.setUnreadCount(messageRepository.countUnreadMessages(chat.getId(), userId));

        return dto;
    }

    private ChatDTO convertToDTO(Chat chat) {
        ChatDTO dto = modelMapper.map(chat, ChatDTO.class);
        dto.setParticipantIds(getParticipantIds(chat));
        return dto;
    }

    private List<Long> getParticipantIds(Chat chat) {
        return chat.getChatMembers().stream()
                .map(cm -> cm.getUser().getId())
                .collect(Collectors.toList());
    }

    private void validateParticipants(ChatDTO chatDTO) {
        if (chatDTO.getParticipantIds().size() < 1) {
            throw new IllegalArgumentException("At least one participant required");
        }

        if (chatDTO.getChatType().equals("PRIVATE")
                && chatDTO.getParticipantIds().size() != 1) {
            throw new IllegalArgumentException("Private chat requires exactly one other participant");
        }
    }

    private void addParticipants(Chat chat, List<Long> participantIds) {
        participantIds.forEach(id -> {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            addChatMember(chat, user);
        });
    }

    private void addCreatorAsMember(Chat chat, User creator) {
        if (!chat.getChatMembers().stream()
                .anyMatch(cm -> cm.getUser().equals(creator))) {
            addChatMember(chat, creator);
        }
    }

    private void addChatMember(Chat chat, User user) {
        ChatMember member = new ChatMember();
        member.setChat(chat);
        member.setUser(user);
        member.setJoinedWhen(LocalDateTime.now());
        chat.getChatMembers().add(member);
    }
}

