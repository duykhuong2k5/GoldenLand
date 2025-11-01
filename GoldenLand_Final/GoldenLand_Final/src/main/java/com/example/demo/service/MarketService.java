package com.example.demo.service;

import com.example.demo.model.dto.market.PriceHistoryResp;

public interface MarketService {
    PriceHistoryResp getHistory(Long buildingId, String range);
}
