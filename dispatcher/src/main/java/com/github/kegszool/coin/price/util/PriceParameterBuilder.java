package com.github.kegszool.coin.price.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.LocalizationService;
import com.github.kegszool.coin.price.model.PriceParameter;
import com.github.kegszool.coin.price.menu.PriceMenuProperties;

import java.util.Map;
import java.util.LinkedHashMap;

@Component
public class PriceParameterBuilder {

    private final String menuName;
    private final LocalizationService localizationService;

    @Autowired
    public PriceParameterBuilder(
            @Value("${menu.price_snapshot.name}") String menuName,
            LocalizationService localizationService
    ) {
        this.menuName = menuName;
        this.localizationService = localizationService;
    }

    public Map<String, PriceParameter> createParameterMap(PriceMenuProperties properties) {
        Map<String, PriceParameter> parameters = new LinkedHashMap<>();

        addLastPriceParameter(parameters, properties);
        addHighestPriceParameter(parameters, properties);
        addLowestPriceParameter(parameters, properties);
        addTradingVolumeParameter(parameters, properties);
        addTradingVolumeCurrencyParameter(parameters, properties);

        return parameters;
    }

    private void addLastPriceParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties) {
        String description = getLocalizedDescription(properties.getLastPriceName());
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> "$" + snapshot.getLastPrice()
        );
        parameters.put(properties.getLastPriceName(), param);
    }

    private void addHighestPriceParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties) {
        String description = getLocalizedDescription(properties.getHighestPrice24hName());
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> "$" + snapshot.getMaxPrice24h()
        );
        parameters.put(properties.getHighestPrice24hName(), param);
    }

    private void addLowestPriceParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties) {
        String description = getLocalizedDescription(properties.getLowestPrice24hName());
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> "$" + snapshot.getMinPrice24h()
        );
        parameters.put(properties.getLowestPrice24hName(), param);
    }

    private void addTradingVolumeParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties) {
        String description = getLocalizedDescription(properties.getTradingVolumeName());
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> String.valueOf(snapshot.getTradingVolume24h())
        );
        parameters.put(properties.getTradingVolumeName(), param);
    }

    private void addTradingVolumeCurrencyParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties) {
        String description = getLocalizedDescription(properties.getTradingVolumeCurrencyName());
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> String.valueOf(snapshot.getTradingVolumeCurrency24h())
        );
        parameters.put(properties.getTradingVolumeCurrencyName(), param);
    }

    private String getLocalizedDescription(String parameterName) {
        return localizationService.getAnswerMessage(menuName, parameterName);
    }
}