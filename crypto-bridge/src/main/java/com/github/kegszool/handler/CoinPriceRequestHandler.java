package com.github.kegszool.handler;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducerService;
import com.github.kegszool.controller.RestCryptoController;
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
    public void handle(ServiceMessage serviceMessage, ResponseProducerService responseProducerService) {
        String coinName = serviceMessage.getData();
        String response = restCryptoController.getCryptoPrice(coinName);
        serviceMessage.setData(response);
        responseProducerService.produce(serviceMessage, COIN_PRICE_RESPONSE_ROUTING_KEY);
    }
}
