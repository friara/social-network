package com.example.social_network01.service.message;

import com.example.social_network01.dto.FileDTO;
import com.example.social_network01.dto.MessageDTO;
import com.example.social_network01.exception.custom.MessageNotFoundException;
import com.example.social_network01.model.Chat;
import com.example.social_network01.model.File;
import com.example.social_network01.model.Message;
import com.example.social_network01.model.User;
import com.example.social_network01.model.events.NewMessageEvent;
import com.example.social_network01.repository.MessageRepository;
import com.example.social_network01.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public MessageDTO createMessage(String text, User user, Chat chat, List<MultipartFile> files) {
        Message message = new Message();
        message.setText(text);
        message.setUser(user);
        message.setChat(chat);
        message.setCreatedWhen(LocalDateTime.now());
        message.setStatus(Message.MessageStatus.SENT);

        // Сохраняем сообщение, чтобы получить ID
        message = messageRepository.save(message);

        // Сохраняем файлы и связываем с сообщением
        if (files != null && !files.isEmpty()) {
            List<FileDTO> fileDTOs = fileService.saveFiles(files, message);
            message.getFiles().addAll(fileDTOs.stream()
                    .map(dto -> modelMapper.map(dto, File.class))
                    .collect(Collectors.toList()));
        }
        eventPublisher.publishEvent(new NewMessageEvent(this, message, user.getId()));
        return modelMapper.map(message, MessageDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDTO> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDTO getMessageById(Long id) {
        return messageRepository.findById(id)
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + id));

        // Удаляем связанные файлы
        if (!message.getFiles().isEmpty()) {
            fileService.deleteFiles(message.getFiles());
        }

        messageRepository.delete(message);
    }

    @Override
    @Transactional
    public MessageDTO updateMessage(Long id, String newText, List<MultipartFile> newFiles) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + id));

        message.setText(newText);
        message.setStatus(Message.MessageStatus.EDITED);

        // Обновляем файлы
        if (newFiles != null && !newFiles.isEmpty()) {
            fileService.deleteFiles(message.getFiles());
            List<FileDTO> newFileDTOs = fileService.saveFiles(newFiles, message);
            message.setFiles(newFileDTOs.stream()
                    .map(dto -> modelMapper.map(dto, File.class))
                    .collect(Collectors.toList()));
        }

        return modelMapper.map(messageRepository.save(message), MessageDTO.class);
    }
}