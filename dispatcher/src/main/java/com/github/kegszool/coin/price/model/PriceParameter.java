package com.github.kegszool.coin.price.model;

import com.github.kegszool.coin.dto.CoinPrice;
import lombok.Getter;

import java.util.function.Function;

public class PriceParameter {

    @Getter
    private final String description;
    private final Function<CoinPrice, String> valueProvider;

    public PriceParameter(
            String description,
            Function<CoinPrice, String> valueProvider
    ) {
        this.description = description;
        this.valueProvider = valueProvider;
    }

    public String getValue(CoinPrice coinPrice) {
        return valueProvider.apply(coinPrice);
    }
}