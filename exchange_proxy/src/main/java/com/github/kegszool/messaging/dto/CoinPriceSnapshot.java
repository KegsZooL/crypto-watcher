package com.github.kegszool.messaging.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CoinPriceSnapshot {
    private String name;
    private double lastPrice;
    private double maxPrice24h;
    private double minPrice24h;
    private BigDecimal tradingVolume24h;
}
