package com.github.kegszool.coin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoinPrice {
    private String name;
    private double lastPrice;
    private double maxPrice24h;
    private double minPrice24h;
    private BigDecimal tradingVolume24h;
    private BigDecimal tradingVolumeCurrency24h;
}