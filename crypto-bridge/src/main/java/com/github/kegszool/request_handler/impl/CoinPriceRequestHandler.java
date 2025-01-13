package com.github.kegszool.request_handler.impl;

import com.github.kegszool.DTO.DataTransferObject;
import com.github.kegszool.communication_service.ResponseProducerService;
import com.github.kegszool.controller.RestCryptoController;
import com.github.kegszool.request_handler.RequestHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoinPriceRequestHandler implements RequestHandler {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response_key}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request_key}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    private final RestCryptoController restCryptoController;

    public CoinPriceRequestHandler(RestCryptoController restCryptoController) {
        this.restCryptoController = restCryptoController;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return COIN_PRICE_REQUEST_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public void handle(DataTransferObject dataTransferObject, ResponseProducerService responseProducerService) {
        String coinName = dataTransferObject.getData();
        String response = restCryptoController.getCryptoPrice(coinName);
        dataTransferObject.setData(response);
        responseProducerService.produce(dataTransferObject, COIN_PRICE_RESPONSE_ROUTING_KEY);
    }
}
