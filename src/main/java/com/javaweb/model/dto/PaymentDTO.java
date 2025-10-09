package com.javaweb.model.dto;

import com.javaweb.entity.PaymentEntity;
import java.time.LocalDateTime;

public class PaymentDTO {

    private Long id;
    private Long transactionId;
    private PaymentEntity.Method method;
    private PaymentEntity.Status status;
    private Double amount;
    private LocalDateTime paymentTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentEntity.Method getMethod() {
        return method;
    }

    public void setMethod(PaymentEntity.Method method) {
        this.method = method;
    }

    public PaymentEntity.Status getStatus() {
        return status;
    }

    public void setStatus(PaymentEntity.Status status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
}
