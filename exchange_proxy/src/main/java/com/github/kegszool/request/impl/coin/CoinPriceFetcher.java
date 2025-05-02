package com.github.kegszool.request.impl.coin;

import com.github.kegszool.exception.json.InvalidJsonFormatException;
import com.github.kegszool.exception.json.JsonFieldNotFoundException;
import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.utils.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Log4j2
@Component
@RequiredArgsConstructor
public class CoinPriceFetcher {

    private final RestCryptoController restCryptoController;
    private final JsonParser jsonParser;

    @Value("${api.exchange.url.market.ticker.base}")
    private String baseRequestUrl;

    @Value("${api.exchange.url.market.ticker.response_parameter.last_price}")
    private String lastPriceField;

    private static final double DEFAULT_DOUBLE_VALUE = -1.0;

    public double getPrice(String coinName) {
        String url = baseRequestUrl + coinName;
        try {
            String response = restCryptoController.getResponse(url);
            String priceStr = jsonParser.parse(response, lastPriceField);
            return parseDoubleSafe(priceStr, lastPriceField);
        } catch (RestClientException | JsonFieldNotFoundException | InvalidJsonFormatException ex) {
            log.error("Error when receiving the coin price {}: {}", coinName, ex.getMessage());
            return DEFAULT_DOUBLE_VALUE;
        }
    }

    private double parseDoubleSafe(String value, String fieldName) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException | NullPointerException ex) {
            log.warn("Couldn't parse the {} field with the value '{}'", fieldName, value);
            return DEFAULT_DOUBLE_VALUE;
        }
    }
}
