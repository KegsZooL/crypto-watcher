package com.github.kegszool.coin.price.model;

import lombok.Getter;

import java.util.function.Function;

public class PriceParameter {

    @Getter
    private final String description;
    private final Function<PriceSnapshot, String> valueProvider;

    public PriceParameter(
            String description,
            Function<PriceSnapshot, String> valueProvider
    ) {
        this.description = description;
        this.valueProvider = valueProvider;
    }

    public String getValue(PriceSnapshot priceSnapshot) {
        return valueProvider.apply(priceSnapshot);
    }
}