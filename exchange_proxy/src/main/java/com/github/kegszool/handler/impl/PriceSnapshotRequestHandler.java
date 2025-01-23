package com.github.kegszool.handler.impl;

import com.github.kegszool.exception.json.InvalidJsonFormatException;
import com.github.kegszool.exception.json.JsonFieldNotFoundException;
import com.github.kegszool.exception.request.price_snapshot.PriceSnapshotRequestException;
import com.github.kegszool.handler.BaseRequestHandler;
import com.github.kegszool.messaging.dto.CoinPriceSnapshot;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.controller.RestCryptoController;
import com.github.kegszool.utils.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.function.Function;

@Component
@Log4j2
public class PriceSnapshotRequestHandler extends BaseRequestHandler {

    @Value("${api.exchange.url.market.ticker.base}")
    private String BASE_REQUEST_URL;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    @Value("${api.exchange.url.market.ticker.response_parameter.last_price}")
    private String LAST_PRICE_FIELD;

    @Value("${api.exchange.url.market.ticker.response_parameter.max_price_24h}")
    private String MAX_PRICE_24H_FIELD;

    @Value("${api.exchange.url.market.ticker.response_parameter.min_price_24h}")
    private String MIN_PRICE_24H_FIELD;

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
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_REQUEST_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public String getResponseRoutingKey() {
        return COIN_PRICE_RESPONSE_ROUTING_KEY;
    }

    @Override
    public ServiceMessage<?> handle(ServiceMessage<String> serviceMessage) {

        String coinName = serviceMessage.getData();
        String requestUrl = BASE_REQUEST_URL + coinName;

        ServiceMessage<CoinPriceSnapshot> responseServiceMessage = new ServiceMessage<>();
        try {
            String response = restCryptoController.getResponse(requestUrl);
            CoinPriceSnapshot snapshot = createPriceSnapshot(response, coinName);

            responseServiceMessage.setData(snapshot);
            responseServiceMessage.setChatId(serviceMessage.getChatId());

        } catch (RestClientException | JsonFieldNotFoundException | InvalidJsonFormatException ex ) {
            throw processErrorOfReceivingPrice(requestUrl, ex);
        }
        return responseServiceMessage;
    }

    private CoinPriceSnapshot createPriceSnapshot(String response, String coinName)
            throws JsonFieldNotFoundException, InvalidJsonFormatException
    {
        var lastPriceStr = jsonParser.parse(response, LAST_PRICE_FIELD);
        double lastPrice = tryParseDouble(
                lastPriceStr, Double::parseDouble, LAST_PRICE_FIELD, DEFAULT_DOUBLE_VALUE, Double.class);

        var maxPrice24hStr = jsonParser.parse(response, MAX_PRICE_24H_FIELD);
        double maxPrice24h = tryParseDouble(
                maxPrice24hStr, Double::parseDouble, MAX_PRICE_24H_FIELD, DEFAULT_DOUBLE_VALUE, Double.class);

        var minPrice24hStr = jsonParser.parse(response, MIN_PRICE_24H_FIELD);
        double minPrice24h = tryParseDouble(
                minPrice24hStr, Double::parseDouble, MIN_PRICE_24H_FIELD, DEFAULT_DOUBLE_VALUE, Double.class);

        var tradingVolumeStr = jsonParser.parse(response, TRADING_VOLUME_FIELD);
        BigDecimal tradingVolume = tryParseDouble(
                tradingVolumeStr, BigDecimal::new, TRADING_VOLUME_FIELD, BigDecimal.ZERO, BigDecimal.class);

        var tradingVolumeCurrencyStr = jsonParser.parse(response, TRADING_VOLUME_CURRENCY_FIELD);
        BigDecimal tradingVolumeCurrency = tryParseDouble(
                tradingVolumeCurrencyStr, BigDecimal::new, TRADING_VOLUME_CURRENCY_FIELD, BigDecimal.ZERO, BigDecimal.class);

        var snapshost = new CoinPriceSnapshot(
                coinName, lastPrice, maxPrice24h, minPrice24h, tradingVolume, tradingVolumeCurrency
        );
        return snapshost;
    }

    private <T> T tryParseDouble(
            String str, Function<String, T> parser,
            String fieldName , T defaultValue, Class<T> clazz
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