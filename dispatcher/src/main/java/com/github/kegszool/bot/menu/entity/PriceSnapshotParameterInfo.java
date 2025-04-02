package com.github.kegszool.bot.menu.entity;

import com.github.kegszool.messaging.dto.command_entity.CoinPriceSnapshot;
import lombok.Getter;

import java.util.function.Function;

public class PriceSnapshotParameterInfo {

    @Getter
    private final String description;
    private final Function<CoinPriceSnapshot, String> valueProvider;

    public PriceSnapshotParameterInfo(
            String description,
            Function<CoinPriceSnapshot, String> valueProvider
    ) {
        this.description = description;
        this.valueProvider = valueProvider;
    }

    public String getValue(CoinPriceSnapshot coinPriceSnapshot) {
        return valueProvider.apply(coinPriceSnapshot);
    }
}