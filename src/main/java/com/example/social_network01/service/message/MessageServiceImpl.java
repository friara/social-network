package com.example.social_network01.service.message;

import com.example.social_network01.dto.message.MessageDTO;
import com.example.social_network01.dto.message.MessageRequestDTO;
import com.example.social_network01.exception.custom.ChatNotFoundException;
import com.example.social_network01.exception.custom.MessageNotFoundException;
import com.example.social_network01.exception.custom.UserNotFoundException;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.Message;
import com.example.social_network01.model.User;
import com.example.social_network01.model.events.NewMessageEvent;
import com.example.social_network01.repository.ChatRepository;
import com.example.social_network01.repository.MessageRepository;
import com.example.social_network01.repository.UserRepository;
import com.example.social_network01.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

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
        message.setCreatedWhen(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        // Обработка вложений
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            fileService.saveFiles(request.getFiles(), savedMessage);
        }

        eventPublisher.publishEvent(new NewMessageEvent(this, message, user.getId()));
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

        // Обновление вложений
        if (request.getFiles() != null) {
            fileService.updateFiles(request.getFiles(), message);
        }

        return modelMapper.map(messageRepository.save(message), MessageDTO.class);
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id " + messageId));

        if (!message.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User is not message author");
        }

        message.setStatus(Message.MessageStatus.DELETED);
        messageRepository.save(message);

        // Опционально: физическое удаление
        // messageRepository.delete(message);
    }

    @Override
    public boolean isMessageAuthor(Long messageId, Long userId) {
        return messageRepository.existsByIdAndUserId(messageId, userId);
    }

    //    private final MessageRepository messageRepository;
//    private final ModelMapper modelMapper;
//    private final FileService fileService;
//    private final ApplicationEventPublisher eventPublisher;
//
//    @Override
//    @Transactional
//    public MessageDTO createMessage(String text, User user, Chat chat, List<MultipartFile> files) {
//        Message message = new Message();
//        message.setText(text);
//        message.setUser(user);
//        message.setChat(chat);
//        message.setCreatedWhen(LocalDateTime.now());
//        message.setStatus(Message.MessageStatus.SENT);
//
//        // Сохраняем сообщение, чтобы получить ID
//        message = messageRepository.save(message);
//
//        // Сохраняем файлы и связываем с сообщением
//        if (files != null && !files.isEmpty()) {
//            List<FileDTO> fileDTOs = fileService.saveFiles(files, message);
//            message.getFiles().addAll(fileDTOs.stream()
//                    .map(dto -> modelMapper.map(dto, File.class))
//                    .collect(Collectors.toList()));
//        }
//        eventPublisher.publishEvent(new NewMessageEvent(this, message, user.getId()));
//        return modelMapper.map(message, MessageDTO.class);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<MessageDTO> getAllMessages() {
//        return messageRepository.findAll().stream()
//                .map(message -> modelMapper.map(message, MessageDTO.class))
//                .collect(Collectors.toList());
//    }
//
    @Override
    @Transactional(readOnly = true)
    public MessageDTO getMessageById(Long id) {
        return messageRepository.findById(id)
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + id));
    }
//
//    @Override
//    @Transactional
//    public void deleteMessage(Long id) {
//        Message message = messageRepository.findById(id)
//                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + id));
//
//        // Удаляем связанные файлы
//        if (!message.getFiles().isEmpty()) {
//            fileService.deleteFiles(message.getFiles());
//        }
//
//        messageRepository.delete(message);
//    }
//
//    @Override
//    @Transactional
//    public MessageDTO updateMessage(Long id, String newText, List<MultipartFile> newFiles) {
//        Message message = messageRepository.findById(id)
//                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + id));
//
//        message.setText(newText);
//        message.setStatus(Message.MessageStatus.EDITED);
//
//        // Обновляем файлы
//        if (newFiles != null && !newFiles.isEmpty()) {
//            fileService.deleteFiles(message.getFiles());
//            List<FileDTO> newFileDTOs = fileService.saveFiles(newFiles, message);
//            message.setFiles(newFileDTOs.stream()
//                    .map(dto -> modelMapper.map(dto, File.class))
//                    .collect(Collectors.toList()));
//        }
//
//        return modelMapper.map(messageRepository.save(message), MessageDTO.class);
//    }
}