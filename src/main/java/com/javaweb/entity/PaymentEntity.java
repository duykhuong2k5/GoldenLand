package com.javaweb.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long transactionId;
    private String method; // COD, VNPAY, MOMO
    private String status; // success, failed, pending
    private Double amount;
    private String vnpTxnRef; // mã giao dịch VNPAY/MOMO
    private LocalDateTime createdAt;
    // getters/setters
}
