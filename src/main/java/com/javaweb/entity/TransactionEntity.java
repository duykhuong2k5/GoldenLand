package com.javaweb.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long propertyId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status; // mới, xác nhận, đang thuê, kết thúc, hủy, hoàn tiền
    private Double totalAmount;
    private LocalDateTime createdAt;
    // getters/setters
}
