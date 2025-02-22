package com.github.kegszool.bot.menu.command.callback.impl.entity;

import com.github.kegszool.messaging.dto.command_entity.PriceSnapshot;
import lombok.Getter;

import java.util.function.Function;

public class PriceSnapshotParameterInfo {

    @Getter
    private final String description;
    private final Function<PriceSnapshot, String> valueProvider;

    public PriceSnapshotParameterInfo(
            String description,
            Function<PriceSnapshot, String> valueProvider
    ) {
        this.description = description;
        this.valueProvider = valueProvider;
    }

    public String getValue(PriceSnapshot coinPriceSnapshot) {
        return valueProvider.apply(coinPriceSnapshot);
    }
}