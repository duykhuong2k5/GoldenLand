package com.javaweb.entity;

import javax.persistence.*;

@Entity
@Table(name = "promotion")
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Thông tin khuyến mãi
}
