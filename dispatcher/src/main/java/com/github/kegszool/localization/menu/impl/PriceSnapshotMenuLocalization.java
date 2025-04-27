package com.github.kegszool.localization.menu.impl;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.localization.menu.BaseMenuLocalization;

@Component
public class PriceSnapshotMenuLocalization extends BaseMenuLocalization {

    public PriceSnapshotMenuLocalization(
        @Value("${menu.price_snapshot.name}") String name,
        @Value("${menu.price_snapshot.title.ru}") String titleRu,
        @Value("${menu.price_snapshot.title.en}") String titleEn,
        @Value("${menu.price_snapshot.sections.ru}") String sectionsRu,
        @Value("${menu.price_snapshot.sections.en}") String sectionsEn,

        @Value("${menu.price_snapshot.parameters.last_price.name}") String lastPriceName,
        @Value("${menu.price_snapshot.parameters.last_price.description.ru}") String lastPriceDescRu,
        @Value("${menu.price_snapshot.parameters.last_price.description.en}") String lastPriceDescEn,

        @Value("${menu.price_snapshot.parameters.highest_price_24h.name}") String highestPriceName,
        @Value("${menu.price_snapshot.parameters.highest_price_24h.description.ru}") String highestPriceDescRu,
        @Value("${menu.price_snapshot.parameters.highest_price_24h.description.en}") String highestPriceDescEn,

        @Value("${menu.price_snapshot.parameters.lowest_price_24h.name}") String lowestPriceName,
        @Value("${menu.price_snapshot.parameters.lowest_price_24h.description.ru}") String lowestPriceDescRu,
        @Value("${menu.price_snapshot.parameters.lowest_price_24h.description.en}") String lowestPriceDescEn,

        @Value("${menu.price_snapshot.parameters.trading_volume.name}") String tradingVolumeName,
        @Value("${menu.price_snapshot.parameters.trading_volume.description.ru}") String tradingVolumeDescRu,
        @Value("${menu.price_snapshot.parameters.trading_volume.description.en}") String tradingVolumeDescEn,

        @Value("${menu.price_snapshot.parameters.trading_volume_currency.name}") String tradingVolumeCurrencyName,
        @Value("${menu.price_snapshot.parameters.trading_volume_currency.description.ru}") String tradingVolumeCurrencyDescRu,
        @Value("${menu.price_snapshot.parameters.trading_volume_currency.description.en}") String tradingVolumeCurrencyDescEn
    ) {
        super(
                name,
                Map.of(
                        "ru", titleRu,
                        "en", titleEn
                ),
                Map.of(
                        "ru", sectionsRu,
                        "en", sectionsEn
                ),
                Map.of(
                        lastPriceName, Map.of(
                                "ru", lastPriceDescRu,
                                "en", lastPriceDescEn
                        ),
                        highestPriceName, Map.of(
                                "ru", highestPriceDescRu,
                                "en", highestPriceDescEn
                        ),
                        lowestPriceName, Map.of(
                                "ru", lowestPriceDescRu,
                                "en", lowestPriceDescEn
                        ),
                        tradingVolumeName, Map.of(
                                "ru", tradingVolumeDescRu,
                                "en", tradingVolumeDescEn
                        ),
                        tradingVolumeCurrencyName, Map.of(
                                "ru", tradingVolumeCurrencyDescRu,
                                "en", tradingVolumeCurrencyDescEn
                        )
                )
        );
    }
}