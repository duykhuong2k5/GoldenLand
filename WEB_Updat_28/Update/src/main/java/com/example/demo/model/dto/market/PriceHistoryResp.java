package com.example.demo.model.dto.market;

import java.util.List;

public record PriceHistoryResp(
        Double latestPopular,
        Double yoyChangePct,
        Double belowPeakPct,
        List<PricePoint> series
) {}
