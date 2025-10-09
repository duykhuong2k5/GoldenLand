package com.javaweb.model.dto;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private Long userId;
    private Long propertyId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    // getters/setters
}
