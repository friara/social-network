package com.example.social_network01.service.message;

import com.example.social_network01.dto.MessageDTO;
import com.example.social_network01.model.Message;
import com.example.social_network01.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MessageDTO createMessage(MessageDTO messageDTO) {
        Message message = modelMapper.map(messageDTO, Message.class);
        return modelMapper.map(messageRepository.save(message), MessageDTO.class);
    }

    @Override
    public List<MessageDTO> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MessageDTO getMessageById(Long id) {
        return messageRepository.findById(id)
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .orElse(null);
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
