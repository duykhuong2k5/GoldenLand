package com.javaweb.model.dto;

import java.time.LocalDateTime;

public class PaymentDTO {
    private Long id;
    private Long transactionId;
    private String method;
    private String status;
    private Double amount;
    private String vnpTxnRef;
    private LocalDateTime createdAt;
    // getters/setters
}
