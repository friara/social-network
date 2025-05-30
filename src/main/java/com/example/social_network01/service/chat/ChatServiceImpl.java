package com.example.social_network01.service.chat;

import com.example.social_network01.dto.chat.ChatDTO;
import com.example.social_network01.dto.chat.ChatSummaryDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("chatService")
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
        //chat.setCreatedWhen(LocalDateTime.now());

        addParticipants(chat, chatDTO.getParticipantIds());
        addCreatorAsMember(chat, creator);

        if (chat.getChatType() == Chat.ChatType.PRIVATE)
        {
            User otherUser = userRepository.findById(chatDTO.getParticipantIds().get(0))
                    .orElseThrow(() -> new UserNotFoundException("User for chat not found"));
            chat.setChatName(creator.getUsername() + '|' + otherUser.getUsername());
        }


        return convertToDTO(chatRepository.save(chat), creator.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ChatDTO getChatById(Long id, User user) {
        return chatRepository.findById(id)
                .map(chat -> convertToDTO(chat, user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
    }

    @Override
    @Transactional
    public ChatDTO updateChat(Long id, ChatDTO chatDTO, User creator) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        modelMapper.map(chatDTO, chat);
        return convertToDTO(chatRepository.save(chat), creator.getId());
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

    @Override
    public ChatDTO deleteParticipant(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        // Поиск участника по userId
        ChatMember memberToRemove = chat.getChatMembers().stream()
                .filter(member -> member.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the chat"));

        // Запрет удаления создателя чата
        if (chat.getCreatedBy().getId().equals(userId) && chat.getChatType() == Chat.ChatType.GROUP) {
            chatRepository.delete(chat); // Удаление всего чата из БД
            return null; // Возвращаем null как индикатор удаления чата
        }

        // Удаление участника
        chat.getChatMembers().remove(memberToRemove);

        if(chat.getChatMembers().isEmpty()) {
            chatRepository.delete(chat); // Удаление всего чата из БД
            return null; // Возвращаем null как индикатор удаления чата
        }

        return convertToDTO(chatRepository.save(chat), chat.getCreatedBy().getId());
    }


    @Override
    @Transactional
    public ChatDTO addParticipants(Long chatId, List<Long> userIds) {
        // Проверка входных данных
        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("User IDs list cannot be null or empty");
        }

        // Удаление дубликатов
        List<Long> distinctUserIds = userIds.stream()
                .distinct()
                .collect(Collectors.toList());

        // Поиск чата
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        // Проверка типа чата
        if (chat.getChatType() != Chat.ChatType.GROUP) {
            throw new IllegalArgumentException("Only group chats can have participants added");
        }

        // Получение существующих участников
        Set<Long> existingMemberIds = chat.getChatMembers().stream()
                .map(member -> member.getUser().getId())
                .collect(Collectors.toSet());

        // Загрузка пользователей
        List<User> usersToAdd = userRepository.findAllById(distinctUserIds);

        // Проверка существования всех пользователей
        if (usersToAdd.size() != distinctUserIds.size()) {
            Set<Long> foundIds = usersToAdd.stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = distinctUserIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());

            throw new ResourceNotFoundException("Users not found with IDs: " + missingIds);
        }

        // Создание новых участников
        List<ChatMember> newMembers = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (User user : usersToAdd) {
            Long userId = user.getId();

            // Пропуск существующих участников
            if (existingMemberIds.contains(userId)) {
                continue;
            }

            // Пропуск создателя чата (если вдруг добавлен)
            if (userId.equals(chat.getCreatedBy().getId())) {
                continue;
            }

            ChatMember member = new ChatMember();
            member.setChat(chat);
            member.setUser(user);
            member.setJoinedWhen(now);
            newMembers.add(member);
        }

        // Добавление новых участников
        if (!newMembers.isEmpty()) {
            chat.getChatMembers().addAll(newMembers);
            chatRepository.save(chat);
        }

        return convertToDTO(chat, chat.getCreatedBy().getId());
    }

    @Override
    public ChatDTO updateChatName(Long chatId, String newName) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        // Запрет удаления в личных чатах
        if (chat.getChatType() == Chat.ChatType.PRIVATE) {
            throw new IllegalArgumentException("Forbidden to update name of personal chats");
        }

        chat.setChatName(newName);

        return convertToDTO(chatRepository.save(chat), chat.getCreatedBy().getId());
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

        // Определяем отображаемое имя чата
        if (chat.getChatType() == Chat.ChatType.PRIVATE) {
            String otherUserFio = chat.getChatMembers().stream()
                    .map(ChatMember::getUser)
                    .filter(user -> !user.getId().equals(userId))
                    .findFirst()
                    .map(user -> user.getFirstName() + " " + user.getLastName())
                    .orElse("Удаленный чат");
            dto.setChatName(otherUserFio);
        }

        return dto;
    }

    private ChatDTO convertToDTO(Chat chat, Long userId) {
        ChatDTO dto = modelMapper.map(chat, ChatDTO.class);

        // Добавляем логику для названия приватного чата
        if (chat.getChatType() == Chat.ChatType.PRIVATE) {
            String otherUserFio = chat.getChatMembers().stream()
                    .map(ChatMember::getUser)
                    .filter(user -> !user.getId().equals(userId))
                    .findFirst()
                    .map(user -> user.getFirstName() + " " + user.getLastName())
                    .orElse("Удаленный чат");
            dto.setChatName(otherUserFio);
        }

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

