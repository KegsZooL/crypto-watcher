package com.github.kegszool.messaging.dto.command_entity;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceSnapshot {
    private String name;
    private BigDecimal lastPrice;
    private BigDecimal maxPrice24h;
    private BigDecimal minPrice24h;
    private BigDecimal tradingVolume24h;
    private BigDecimal tradingVolumeCurrency24h;
}