package com.javaweb.model.response;

import com.javaweb.entity.PaymentEntity;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long paymentId;
    private PaymentEntity.Status status;
    private String message;
    private String redirectUrl;
    private LocalDateTime paymentTime;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentEntity.Status getStatus() {
        return status;
    }

    public void setStatus(PaymentEntity.Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
}
