package com.github.kegszool.coin.price.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.localization.LocalizationService;
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

    public Map<String, PriceParameter> createParameterMap(PriceMenuProperties properties, String chatId) {
        Map<String, PriceParameter> parameters = new LinkedHashMap<>();

        addLastPriceParameter(parameters, properties, chatId);
        addHighestPriceParameter(parameters, properties, chatId);
        addLowestPriceParameter(parameters, properties, chatId);
        addTradingVolumeParameter(parameters, properties, chatId);
        addTradingVolumeCurrencyParameter(parameters, properties, chatId);

        return parameters;
    }

    private void addLastPriceParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties, String chatId) {
        String description = getLocalizedDescription(properties.getLastPriceName(), chatId);
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> "$" + snapshot.getLastPrice()
        );
        parameters.put(properties.getLastPriceName(), param);
    }

    private void addHighestPriceParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties, String chatId) {
        String description = getLocalizedDescription(properties.getHighestPrice24hName(), chatId);
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> "$" + snapshot.getMaxPrice24h()
        );
        parameters.put(properties.getHighestPrice24hName(), param);
    }

    private void addLowestPriceParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties, String chatId) {
        String description = getLocalizedDescription(properties.getLowestPrice24hName(), chatId);
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> "$" + snapshot.getMinPrice24h()
        );
        parameters.put(properties.getLowestPrice24hName(), param);
    }

    private void addTradingVolumeParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties, String chatId) {
        String description = getLocalizedDescription(properties.getTradingVolumeName(), chatId);
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> String.valueOf(snapshot.getTradingVolume24h())
        );
        parameters.put(properties.getTradingVolumeName(), param);
    }

    private void addTradingVolumeCurrencyParameter(Map<String, PriceParameter> parameters, PriceMenuProperties properties, String chatId) {
        String description = getLocalizedDescription(properties.getTradingVolumeCurrencyName(), chatId);
        PriceParameter param = new PriceParameter(
                description,
                snapshot -> String.valueOf(snapshot.getTradingVolumeCurrency24h())
        );
        parameters.put(properties.getTradingVolumeCurrencyName(), param);
    }

    private String getLocalizedDescription(String parameterName, String chatId) {
        return localizationService.getAnswerMessage(menuName, parameterName, chatId);
    }
}