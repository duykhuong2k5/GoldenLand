package com.javaweb.service.impl;

import com.javaweb.entity.TransactionEntity;
import com.javaweb.model.dto.TransactionDTO;
import com.javaweb.model.request.TransactionCreateRequest;
import com.javaweb.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionCreateRequest request) {
        TransactionEntity entity = new TransactionEntity();
        entity.setUserId(request.getUserId());
        entity.setPropertyId(request.getPropertyId());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setTotalAmount(request.getTotalAmount());
        entity.setStatus(TransactionEntity.Status.NEW);
        entity.setCreatedAt(LocalDateTime.now());
        return modelMapper.map(transactionRepository.save(entity), TransactionDTO.class);
    }

    public List<TransactionDTO> getRentalHistory(Long userId) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(entity -> modelMapper.map(entity, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<TransactionDTO> updateStatus(Long transactionId, TransactionEntity.Status status) {
        return transactionRepository.findById(transactionId)
                .map(entity -> {
                    entity.setStatus(status);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return modelMapper.map(transactionRepository.save(entity), TransactionDTO.class);
                });
    }
}
