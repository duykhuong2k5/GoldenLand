package com.javaweb.entity;

import javax.persistence.*;

@Entity
@Table(name = "building")
public class BuildingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Thông tin tài sản
}
