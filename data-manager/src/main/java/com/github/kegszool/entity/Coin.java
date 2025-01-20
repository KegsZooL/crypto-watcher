package com.github.kegszool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "coin")
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "last_price", nullable = false, precision = 15, scale = 9)
    private float lastPrice;

    @Column(name = "min_price_24h", nullable = false, precision = 15, scale = 9)
    private float minPrice24h;

    @Column(name = "max_price_24h", nullable = false, precision = 15, scale = 9)
    private float maxPrice24h;

    @Column(name = "trading_volume_24h", nullable = false, precision = 25, scale = 5)
    private BigDecimal tradingVolume24h;
}