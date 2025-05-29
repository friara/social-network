package com.example.social_network01.service.message;

import com.example.social_network01.dto.message.MessageDTO;
import com.example.social_network01.dto.message.MessageRequestDTO;
import com.example.social_network01.exception.custom.ChatNotFoundException;
import com.example.social_network01.exception.custom.MessageNotFoundException;
import com.example.social_network01.exception.custom.UserNotFoundException;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.Message;
import com.example.social_network01.model.User;
import com.example.social_network01.repository.ChatRepository;
import com.example.social_network01.repository.MessageRepository;
import com.example.social_network01.repository.UserRepository;
import com.example.social_network01.service.file.FileService;
import com.example.social_network01.service.notification.ChatNotificationService;
import com.example.social_network01.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service("messageService")
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    private final NotificationService notificationService;

    private final ChatNotificationService chatNotificationService;

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getMessagesByChatId(Long chatId, Pageable pageable) {
        return messageRepository.findAllByChat_Id(chatId, pageable)
                .map(message -> {
                    MessageDTO dto = modelMapper.map(message, MessageDTO.class);
                    dto.setFiles(fileService.getFilesForMessage(message.getId()));
                    return dto;
                });
    }

    @Override
    @Transactional
    public MessageDTO createMessage(Long chatId, Long userId, MessageRequestDTO request) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Chat not found with id " + chatId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setText(request.getText());
        message.setStatus(Message.MessageStatus.SENT);
        //message.setCreatedWhen(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        // Обработка вложений
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            fileService.saveFiles(request.getFiles(), savedMessage);
        }

        notificationService.createNotificationForMessage(message);
        chatNotificationService.notifyChatMembers(savedMessage);

        return modelMapper.map(savedMessage, MessageDTO.class);
    }

    @Override
    @Transactional
    public MessageDTO updateMessage(Long messageId, Long userId, MessageRequestDTO request) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id " + messageId));

        if (!message.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User is not message author");
        }

        message.setText(request.getText());
        message.setStatus(Message.MessageStatus.EDITED);

        if (request.isFileUpload()) {
            fileService.updateFiles(request.getFiles(), message);
        }

        Message savedMessage = new Message();
        if((message.getText() == null || message.getText().isEmpty()) && (message.getFiles() == null || message.getFiles().isEmpty())) {
            deleteMessage(message.getChat().getId(), message.getId());
        }
        else
        { savedMessage = messageRepository.save(message);}

        return modelMapper.map(savedMessage, MessageDTO.class);
    }

    @Override
    @Transactional
    public void deleteMessage(Long chatId, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id " + messageId));

        if(!message.getChat().getId().equals(chatId)) {
            throw new ChatNotFoundException("Chat not found with id " + chatId);
        }

        // Опционально: физическое удаление
         messageRepository.delete(message);
    }

    @Override
    public boolean isMessageAuthor(Long messageId, Long userId) {
        return messageRepository.existsByIdAndUserId(messageId, userId);
    }


    @Override
    @Transactional(readOnly = true)
    public MessageDTO getMessageById(Long id) {
        return messageRepository.findById(id)
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + id));
    }

}