package com.github.kegszool.request_handler.impl;

import com.github.kegszool.communication_service.ResponseProducerService;
import com.github.kegszool.controller.RestCryptoController;
import com.github.kegszool.request_handler.RequestHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoinPriceRequestHandler implements RequestHandler {

    @Value("${coin.prefix}")
    private String COIN_PREFIX;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_key}")
    private String COIN_PRICE_ROUTING_KEY;

    private final RestCryptoController restCryptoController;

    public CoinPriceRequestHandler(RestCryptoController restCryptoController) {
        this.restCryptoController = restCryptoController;
    }

    @Override
    public boolean canHandle(String request) {
        return request.startsWith(COIN_PREFIX);
    }

    @Override
    public void handle(String request, ResponseProducerService responseProducerService) {
        String coinName = getCryptocurrencyNameWithoutCoinPrefix(request);
        String response = restCryptoController.getCryptoPrice(coinName);
        responseProducerService.produce(response, COIN_PRICE_ROUTING_KEY);
    }

    private String getCryptocurrencyNameWithoutCoinPrefix(String cryptocurrencyNameWithPrefix) {
        int lengthOfCoinPrefix = COIN_PREFIX.length();
        int lengthOfData = cryptocurrencyNameWithPrefix.length();
        return cryptocurrencyNameWithPrefix.substring(lengthOfCoinPrefix, lengthOfData);
    }
}
