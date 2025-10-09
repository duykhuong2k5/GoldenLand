package com.javaweb.service.impl;

import com.javaweb.entity.MessageEntity;
import com.javaweb.model.request.ChatMessageRequest;
import com.javaweb.model.response.ChatMessageResponse;
import com.javaweb.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl {

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    public ChatServiceImpl(MessageRepository messageRepository, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
    }

    public ChatMessageResponse saveMessage(ChatMessageRequest request) {
        MessageEntity entity = new MessageEntity();
        entity.setTransactionId(request.getTransactionId());
        entity.setSenderId(request.getSenderId());
        entity.setReceiverId(request.getReceiverId());
        entity.setContent(request.getContent());
        entity.setAttachmentUrl(request.getAttachmentUrl());
        entity.setCreatedAt(LocalDateTime.now());
        return modelMapper.map(messageRepository.save(entity), ChatMessageResponse.class);
    }

    public List<ChatMessageResponse> getConversation(Long transactionId) {
        return messageRepository.findByTransactionIdOrderByCreatedAtAsc(transactionId).stream()
                .map(entity -> modelMapper.map(entity, ChatMessageResponse.class))
                .collect(Collectors.toList());
    }
}
