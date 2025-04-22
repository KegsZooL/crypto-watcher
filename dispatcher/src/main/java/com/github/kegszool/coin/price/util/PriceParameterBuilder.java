package com.github.kegszool.coin.price.util;

import com.github.kegszool.coin.price.model.PriceParameter;
import org.springframework.stereotype.Component;
import com.github.kegszool.coin.price.menu.PriceMenuProperties;

import java.util.Map;

@Component
public class PriceParameterBuilder {

    public Map<String, PriceParameter> createParameterMap(PriceMenuProperties properties) {
        return Map.of(
                properties.getLastPriceName(), new PriceParameter(
                        properties.getLastPriceDescription(), snapshot -> "$" + snapshot.getLastPrice()),
                properties.getHighestPrice24hName(), new PriceParameter(
                        properties.getHighestPrice24hDescription(), snapshot -> "$" + snapshot.getMaxPrice24h()),
                properties.getLowestPrice24hName(), new PriceParameter(
                        properties.getLowestPrice24hDescription(), snapshot -> "$" + snapshot.getMinPrice24h()),
                properties.getTradingVolumeName(), new PriceParameter(
                        properties.getTradingVolumeDescription(), snapshot -> String.valueOf(snapshot.getTradingVolume24h())),
                properties.getTradingVolumeCurrencyName(), new PriceParameter(
                        properties.getTradingVolumeCurrencyDescription(), snapshot -> String.valueOf(snapshot.getTradingVolumeCurrency24h())
                )
        );
    }
}