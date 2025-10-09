package com.javaweb.repository;

import com.javaweb.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    // custom query nếu cần
}
