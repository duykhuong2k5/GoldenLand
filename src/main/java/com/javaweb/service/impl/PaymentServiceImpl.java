package com.javaweb.service.impl;

import com.javaweb.entity.PaymentEntity;
import com.javaweb.model.dto.PaymentDTO;
import com.javaweb.model.request.PaymentRequest;
import com.javaweb.model.response.PaymentResponse;
import com.javaweb.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        PaymentEntity entity = new PaymentEntity();
        entity.setTransactionId(request.getTransactionId());
        entity.setMethod(request.getMethod());
        entity.setAmount(request.getAmount());
        entity.setStatus(PaymentEntity.Status.PENDING);
        entity.setPaymentTime(LocalDateTime.now());
        PaymentEntity saved = paymentRepository.save(entity);

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(saved.getId());
        response.setStatus(saved.getStatus());
        response.setMessage("Payment initiated successfully");
        response.setPaymentTime(saved.getPaymentTime());
        response.setRedirectUrl("https://sandbox.payment-gateway.local/checkout/" + saved.getId());
        return response;
    }

    @Transactional
    public Optional<PaymentDTO> updateStatus(Long paymentId, PaymentEntity.Status status, String gatewayTransactionId) {
        return paymentRepository.findById(paymentId)
                .map(entity -> {
                    entity.setStatus(status);
                    entity.setGatewayTransactionId(gatewayTransactionId);
                    entity.setPaymentTime(LocalDateTime.now());
                    return modelMapper.map(paymentRepository.save(entity), PaymentDTO.class);
                });
    }

    public Optional<PaymentDTO> findByGatewayTransactionId(String gatewayTransactionId) {
        return paymentRepository.findByGatewayTransactionId(gatewayTransactionId)
                .map(entity -> modelMapper.map(entity, PaymentDTO.class));
    }
}
