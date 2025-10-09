package com.javaweb.repository;

import com.javaweb.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {
    // custom query nếu cần
}
