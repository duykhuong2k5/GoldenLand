package com.example.demo.api;

import com.example.demo.model.dto.market.PriceHistoryResp;
import com.example.demo.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketAPI {

    private final MarketService marketService;

    // GET /api/market/price-history?buildingId=1&range=1y|2y|5y
    @GetMapping("/price-history")
    public PriceHistoryResp history(@RequestParam Long buildingId,
                                    @RequestParam(defaultValue = "1y") String range) {
        return marketService.getHistory(buildingId, range);
    }
}
