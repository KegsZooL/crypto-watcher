package com.github.kegszool.handler.impl;

import com.github.kegszool.exception.request.coin.price.CoinPriceExchangeRequestException;
import com.github.kegszool.handler.BaseRequestHandler;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.controller.RestCryptoController;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
@Log4j2
public class CoinPriceRequestHandler extends BaseRequestHandler {

    @Value("${api.exchange.url.market.ticker.base}")
    private String BASE_REQUEST_URL;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    public CoinPriceRequestHandler(RestCryptoController restCryptoController) {
        super(restCryptoController);
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
    public String handle(ServiceMessage serviceMessage) {

        String coin = serviceMessage.getData();
        String requestUrl = BASE_REQUEST_URL + coin;
        String response;

        try {
            response = restCryptoController.getResponse(requestUrl);
        } catch (RestClientException ex) {
            throw processErrorOfReceivingPrice(requestUrl, ex);
        }
        return response;
    }

    private CoinPriceExchangeRequestException processErrorOfReceivingPrice(String requestUrl, Exception ex) {
        log.error("Error executing the GET request to receive the coin price.\n\t\t Request: {}", requestUrl, ex);
        return new CoinPriceExchangeRequestException("Error executing the GET request: " + requestUrl, ex);
    }
}