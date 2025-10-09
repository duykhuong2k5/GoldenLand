package com.javaweb.repository;

import com.javaweb.entity.MessageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByTransactionIdOrderByCreatedAtAsc(Long transactionId);
}
