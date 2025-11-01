package com.example.demo.repository;

import com.example.demo.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    @Query("select p from PriceHistory p where p.building.id = :buildingId order by p.period asc")
    List<PriceHistory> findByBuildingOrderByPeriod(@Param("buildingId") Long buildingId);
}
