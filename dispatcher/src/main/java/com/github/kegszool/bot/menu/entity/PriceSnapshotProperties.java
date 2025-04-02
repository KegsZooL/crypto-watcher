package com.github.kegszool.bot.menu.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PriceSnapshotProperties {

    @Value("${menu.price_snapshot.name}")
    private String PRICE_SNAPSHOT_MENU_NAME;

    @Value("${menu.price_snapshot.parameters.prefix}")
    private String PARAMETER_PREFIX;

    @Value("${menu.price_snapshot.parameters.last_price.name}")
    private String LAST_PRICE_PARAMETER;

    @Value("${menu.price_snapshot.parameters.highest_price_24h.name}")
    private String HIGHEST_PRICE_24H_PARAMETER;

    @Value("${menu.price_snapshot.parameters.lowest_price_24h.name}")
    private String LOWEST_PRICE_24H_PARAMETER;

    @Value("${menu.price_snapshot.parameters.trading_volume.name}")
    private String TRADING_VOLUME_PARAMETER;

    @Value("${menu.price_snapshot.parameters.trading_volume_currency.name}")
    private String TRADING_VOLUME_CURRENCY_PARAMETER;

    @Value("${menu.price_snapshot.parameters.last_price.description}")
    private String LAST_PRICE_PARAMETER_DESCRIPTION;

    @Value("${menu.price_snapshot.parameters.highest_price_24h.description}")
    private String HIGHEST_PRICE_24H_PARAMETER_DESCRIPTION;

    @Value("${menu.price_snapshot.parameters.lowest_price_24h.description}")
    private String LOWEST_PRICE_24H_PARAMETER_DESCRIPTION;

    @Value("${menu.price_snapshot.parameters.trading_volume.description}")
    private String TRADING_VOLUME_PARAMETER_DESCRIPTION;

    @Value("${menu.price_snapshot.parameters.trading_volume_currency.description}")
    private String TRADING_VOLUME_CURRENCY_PARAMETER_DESCRIPTION;

    public String getMenuName() {
        return PRICE_SNAPSHOT_MENU_NAME;
    }

    public String getParameterPrefix() {
        return PARAMETER_PREFIX;
    }

    public String getLastPriceName() {
        return LAST_PRICE_PARAMETER;
    }

    public String getHighestPrice24hName() {
        return HIGHEST_PRICE_24H_PARAMETER;
    }

    public String getLowestPrice24hName() {
        return LOWEST_PRICE_24H_PARAMETER;
    }

    public String getTradingVolumeName() {
        return TRADING_VOLUME_PARAMETER;
    }

    public String getTradingVolumeCurrencyName() {
        return TRADING_VOLUME_CURRENCY_PARAMETER;
    }

    public String getLastPriceDescription() {
        return LAST_PRICE_PARAMETER_DESCRIPTION;
    }

    public String getHighestPrice24hDescription() {
        return HIGHEST_PRICE_24H_PARAMETER_DESCRIPTION;
    }

    public String getLowestPrice24hDescription() {
        return LOWEST_PRICE_24H_PARAMETER_DESCRIPTION;
    }

    public String getTradingVolumeDescription() {
        return TRADING_VOLUME_PARAMETER_DESCRIPTION;
    }

    public String getTradingVolumeCurrencyDescription() {
        return TRADING_VOLUME_CURRENCY_PARAMETER_DESCRIPTION;
    }
}