package com.javaweb.repository;

import com.javaweb.entity.PaymentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByGatewayTransactionId(String gatewayTransactionId);
}
