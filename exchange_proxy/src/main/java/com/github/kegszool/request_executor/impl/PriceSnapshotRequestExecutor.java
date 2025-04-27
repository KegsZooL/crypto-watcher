package com.github.kegszool.request_executor.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

import com.github.kegszool.exception.json.InvalidJsonFormatException;
import com.github.kegszool.exception.json.JsonFieldNotFoundException;
import com.github.kegszool.exception.request.PriceSnapshotRequestException;

import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.rest.RestCryptoController;
import com.github.kegszool.request_executor.BaseRequestExecutor;

import org.springframework.web.client.RestClientException;
import com.github.kegszool.messaging.dto.command_entity.PriceSnapshot;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Log4j2
@Component
public class PriceSnapshotRequestExecutor extends BaseRequestExecutor<String, PriceSnapshot> {

    @Value("${api.exchange.url.market.ticker.base}")
    private String BASE_REQUEST_URL;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${api.exchange.url.market.ticker.response_parameter.last_price}")
    private String LAST_PRICE_FIELD;

    @Value("${api.exchange.url.market.ticker.response_parameter.highest_price_24h}")
    private String HIGHEST_PRICE_24H_FIELD;

    @Value("${api.exchange.url.market.ticker.response_parameter.lowest_price_24h}")
    private String LOWEST_PRICE_24H_FIELD;

    @Value("${api.exchange.url.market.ticker.response_parameter.trading_volume}")
    private String TRADING_VOLUME_FIELD;

    @Value("${api.exchange.url.market.ticker.response_parameter.trading_volume_currency}")
    private String TRADING_VOLUME_CURRENCY_FIELD;

    @Value("${currency_suffix}")
    private String CURRENCY_SUFFIX;

    private static final double DEFAULT_DOUBLE_VALUE = -1.0;

    public PriceSnapshotRequestExecutor(
            RestCryptoController restCryptoController,
            JsonParser jsonParser
    ) {
        super(restCryptoController, jsonParser);
    }

    @Override
    public String getResponseRoutingKey() {
        return COIN_PRICE_RESPONSE_ROUTING_KEY;
    }

    @Override
    public ServiceMessage<PriceSnapshot> execute(ServiceMessage<String> serviceMessage) {

        String coinName = serviceMessage.getData();
        String requestUrl = BASE_REQUEST_URL + coinName;

        ServiceMessage<PriceSnapshot> responseServiceMessage = new ServiceMessage<>();
        try {
            String response = restCryptoController.getResponse(requestUrl);
            PriceSnapshot snapshot = createPriceSnapshot(response, coinName);

            responseServiceMessage.setData(snapshot);
            responseServiceMessage.setMessageId(serviceMessage.getMessageId());
            responseServiceMessage.setChatId(serviceMessage.getChatId());

        } catch (RestClientException | JsonFieldNotFoundException | InvalidJsonFormatException ex) {
            throw createRequestException(requestUrl, ex);
        }
        return responseServiceMessage;
    }

    private PriceSnapshot createPriceSnapshot(String json, String coinName) throws JsonFieldNotFoundException, InvalidJsonFormatException
    {
        double lastPrice = createLastPrice(json);
        double highestPrice = createHighestPrice(json);
        double lowestPrice = createLowestPrice(json);
        BigDecimal tradingVolume = createTradingVolume(json);
        BigDecimal tradingVolumeCurrency = createTradingVolumeCurrency(json);

        String coinNameWithoutCurrencyPrefix = coinName.replace(CURRENCY_SUFFIX, "");
        return new PriceSnapshot(
                coinNameWithoutCurrencyPrefix, lastPrice, highestPrice, lowestPrice, tradingVolume, tradingVolumeCurrency
        );
    }

    private double createLastPrice(String json) throws JsonFieldNotFoundException, InvalidJsonFormatException {
        String value = jsonParser.parse(json, LAST_PRICE_FIELD);
        double lastPrice = tryParseDouble(value, Double::parseDouble,
                LAST_PRICE_FIELD, DEFAULT_DOUBLE_VALUE, Double.class);
        return roundToTwoDecimalPlaces(lastPrice);
    }

    private double createHighestPrice(String json) throws  JsonFieldNotFoundException, InvalidJsonFormatException {
        String value = jsonParser.parse(json, HIGHEST_PRICE_24H_FIELD);
        double highestPrice = tryParseDouble(value, Double::parseDouble,
                HIGHEST_PRICE_24H_FIELD, DEFAULT_DOUBLE_VALUE, Double.class);
        return roundToTwoDecimalPlaces(highestPrice);
    }

    private double createLowestPrice(String json) throws JsonFieldNotFoundException, InvalidJsonFormatException {
        String value = jsonParser.parse(json, LOWEST_PRICE_24H_FIELD);
        double lowestPrice = tryParseDouble(value, Double::parseDouble,
                LOWEST_PRICE_24H_FIELD, DEFAULT_DOUBLE_VALUE, Double.class);
        return roundToTwoDecimalPlaces(lowestPrice);
    }

    private BigDecimal createTradingVolume(String json) throws JsonFieldNotFoundException, InvalidJsonFormatException {
        String value = jsonParser.parse(json, TRADING_VOLUME_FIELD);
        return tryParseDouble(value, BigDecimal::new, TRADING_VOLUME_FIELD,
                BigDecimal.ZERO, BigDecimal.class)
                .setScale(8, RoundingMode.HALF_UP);
    }

    private BigDecimal createTradingVolumeCurrency(String json) throws JsonFieldNotFoundException, InvalidJsonFormatException {
        String value = jsonParser.parse(json, TRADING_VOLUME_CURRENCY_FIELD);
        return tryParseDouble(value, BigDecimal::new, TRADING_VOLUME_CURRENCY_FIELD,
                BigDecimal.ZERO, BigDecimal.class)
                .setScale(8, RoundingMode.HALF_UP);
    }

    private double roundToTwoDecimalPlaces(double value) {
        return BigDecimal.valueOf(value)
                .setScale(12, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private <T> T tryParseDouble(
            String str, Function<String, T> parser,
            String fieldName, T defaultValue, Class<T> clazz
    ) {
        try {
            return parser.apply(str);
        } catch (NullPointerException | NumberFormatException ex) {
            log.warn("Error converting a string \"{}\" to a type \"{}\" in field \"{}\"",
                    str, clazz.getSimpleName(), fieldName);
            return defaultValue;
        }
    }

    private PriceSnapshotRequestException createRequestException(String requestUrl, Exception ex) {
        log.error("Error executing the GET request to receive the coin price.\n\t\t Request: {}", requestUrl, ex);
        return new PriceSnapshotRequestException("Error executing the GET request: " + requestUrl, ex);
    }
}