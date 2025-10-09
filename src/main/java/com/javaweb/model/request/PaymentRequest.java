package com.javaweb.model.request;

import com.javaweb.entity.PaymentEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @NotNull
    private Long transactionId;

    @NotNull
    private PaymentEntity.Method method;

    @NotNull
    @Min(0)
    private Double amount;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
