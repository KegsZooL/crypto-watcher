package com.github.kegszool.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceSnapshot {
    private String name;
    private double lastPrice;
    private double maxPrice24h;
    private double minPrice24h;
    private BigDecimal tradingVolume24h;
    private BigDecimal tradingVolumeCurrency24h;
}