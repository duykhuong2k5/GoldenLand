package com.example.demo.service.impl;

import com.example.demo.entity.PriceHistory;
import com.example.demo.model.dto.market.PriceHistoryResp;
import com.example.demo.model.dto.market.PricePoint;
import com.example.demo.repository.PriceHistoryRepository;
import com.example.demo.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final PriceHistoryRepository priceRepo;

    @Override
    public PriceHistoryResp getHistory(Long buildingId, String range) {
        int quarters = switch (range) {
            case "2y" -> 8;
            case "5y" -> 20;
            default -> 4;
        };

        List<PricePoint> all = priceRepo.findByBuildingOrderByPeriod(buildingId)
                .stream()
                .map(ph -> new PricePoint(
                        ph.getPeriod(),
                        toD(ph.getPriceMin()),
                        toD(ph.getPriceMed()),
                        toD(ph.getPriceMax())
                ))
                .toList();

        List<PricePoint> series = all.size() > quarters
                ? all.subList(all.size() - quarters, all.size())
                : all;

        Double latest = series.isEmpty() ? null : series.get(series.size() - 1).med();
        Double lastYear = series.size() >= 4 ? series.get(series.size() - 4).med() : null;
        Double yoy = (latest != null && lastYear != null && lastYear > 0)
                ? (latest - lastYear) * 100.0 / lastYear
                : null;

        Double peak = series.stream().map(PricePoint::med).filter(Objects::nonNull).max(Double::compare).orElse(null);
        Double belowPeak = (latest != null && peak != null && peak > 0)
                ? (latest - peak) * 100.0 / peak
                : null;

        return new PriceHistoryResp(latest, yoy, belowPeak, series);
    }

    private static Double toD(BigDecimal bd) {
        return bd == null ? null : bd.doubleValue();
    }
}
