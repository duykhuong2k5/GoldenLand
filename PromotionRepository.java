package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.javaweb.entity.PromotionEntity;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {
    // Truy vấn khuyến mãi
}
