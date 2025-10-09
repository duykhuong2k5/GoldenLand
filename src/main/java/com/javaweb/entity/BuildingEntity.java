package com.javaweb.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "buildings")
public class BuildingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String description;
    private String imageUrl;
    private Double price;
    private Long ownerId;
    private LocalDateTime createdAt;
    // getters/setters
}
