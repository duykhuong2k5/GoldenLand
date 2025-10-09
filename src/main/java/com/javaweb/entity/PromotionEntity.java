package com.javaweb.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Double discountPercent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long buildingId;
    // getters/setters
}
