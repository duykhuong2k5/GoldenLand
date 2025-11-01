package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
    name = "price_history",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_building_period",
        columnNames = {"building_id", "period"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceHistory extends Base{

    

    // Mối quan hệ nhiều lịch sử giá thuộc 1 tòa nhà
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    // Ví dụ: "2025Q3"
    @Column(length = 6, nullable = false)
    private String period;

    @Column(name = "price_min", precision = 10, scale = 1)
    private BigDecimal priceMin;

    @Column(name = "price_med", precision = 10, scale = 1)
    private BigDecimal priceMed;

    @Column(name = "price_max", precision = 10, scale = 1)
    private BigDecimal priceMax;

    // Số lượng tin đăng hoặc giao dịch tham khảo trong quý
    private Integer listings;
}
