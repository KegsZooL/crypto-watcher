package com.github.kegszool.handler.impl;

import com.github.kegszool.exception.json.InvalidJsonFormatException;
import com.github.kegszool.exception.json.JsonFieldNotFoundException;
import com.github.kegszool.exception.request.price_snapshot.PriceSnapshotRequestException;

import com.github.kegszool.utils.JsonParser;
import com.github.kegszool.handler.BaseRequestHandler;
import com.github.kegszool.controller.RestCryptoController;

import com.github.kegszool.messaging.dto.command_entity.PriceSnapshot;
import com.github.kegszool.messaging.dto.ServiceMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.function.Function;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class PriceSnapshotRequestHandler extends BaseRequestHandler {

    @Value("${api.exchange.url.market.ticker.base}")
    private String BASE_REQUEST_URL;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

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

    private static final double DEFAULT_DOUBLE_VALUE = -1.0;

    public PriceSnapshotRequestHandler(
            RestCryptoController restCryptoController,
            JsonParser jsonParser
    ) {
        super(restCryptoController, jsonParser);
    }

    @Override
    public String getRequestRoutingKey() {
        return COIN_PRICE_REQUEST_ROUTING_KEY;
    }

    @Override
    public String getResponseRoutingKey() {
        return COIN_PRICE_RESPONSE_ROUTING_KEY;
    }

    @Override
    public ServiceMessage<?> handle(ServiceMessage<String> serviceMessage) {

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
            throw processErrorOfReceivingPrice(requestUrl, ex);
        }
        return responseServiceMessage;
    }

    private PriceSnapshot createPriceSnapshot(String response, String coinName)
            throws JsonFieldNotFoundException, InvalidJsonFormatException
    {
        var lastPriceStr = jsonParser.parse(response, LAST_PRICE_FIELD);
        double lastPrice = tryParseDouble(
                lastPriceStr, Double::parseDouble, LAST_PRICE_FIELD, DEFAULT_DOUBLE_VALUE, Double.class);

        var highestPriceStr = jsonParser.parse(response, HIGHEST_PRICE_24H_FIELD);
        double highestPrice = tryParseDouble(
                highestPriceStr, Double::parseDouble, HIGHEST_PRICE_24H_FIELD, DEFAULT_DOUBLE_VALUE, Double.class);

        var lowestPriceStr = jsonParser.parse(response, LOWEST_PRICE_24H_FIELD);
        double lowestPrice = tryParseDouble(
                lowestPriceStr, Double::parseDouble, LOWEST_PRICE_24H_FIELD, DEFAULT_DOUBLE_VALUE, Double.class);

        var tradingVolumeStr = jsonParser.parse(response, TRADING_VOLUME_FIELD);
        BigDecimal tradingVolume = tryParseDouble(
                tradingVolumeStr, BigDecimal::new, TRADING_VOLUME_FIELD, BigDecimal.ZERO, BigDecimal.class);

        var tradingVolumeCurrencyStr = jsonParser.parse(response, TRADING_VOLUME_CURRENCY_FIELD);
        BigDecimal tradingVolumeCurrency = tryParseDouble(
                tradingVolumeCurrencyStr, BigDecimal::new, TRADING_VOLUME_CURRENCY_FIELD, BigDecimal.ZERO, BigDecimal.class);

        var snapshost = new PriceSnapshot(
                coinName, lastPrice, highestPrice, lowestPrice, tradingVolume, tradingVolumeCurrency
        );
        return snapshost;
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

    private PriceSnapshotRequestException processErrorOfReceivingPrice(String requestUrl, Exception ex) {
        log.error("Error executing the GET request to receive the coin price.\n\t\t Request: {}", requestUrl, ex);
        return new PriceSnapshotRequestException("Error executing the GET request: " + requestUrl, ex);
    }
}