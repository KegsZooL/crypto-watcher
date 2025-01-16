package com.github.kegszool.handler;

import com.github.kegszool.exception.exchange.request.coin.price.CoinPriceExchangeRequestException;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.controller.RestCryptoController;
import com.github.kegszool.utils.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
@Log4j2
public class CoinPriceRequestHandler extends BaseRequestHandler {

    private final static String BASE_URL = "https://www.okx.com/api/v5/market/ticker";

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response_key}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request_key}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    public CoinPriceRequestHandler(RestCryptoController restCryptoController, JsonParser jsonParser) {
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
    public String handle(ServiceMessage serviceMessage) {

        String coinName = serviceMessage.getData();
        String requestUrl = BASE_URL + "?instId=" + coinName;
        String price;

        try {
            String response = restCryptoController.getResponse(requestUrl);
            price = jsonParser.parse(response, "last");
        } catch (RestClientException ex) {
            throw processErrorOfReceivingPrice(requestUrl);
        }
        return price;
    }

    private CoinPriceExchangeRequestException processErrorOfReceivingPrice(String requestUrl) {
        log.error("Error executing the GET request to receive the coin price.\n\t\t Request: {}", requestUrl);
        return new CoinPriceExchangeRequestException("Error executing the GET request: " + requestUrl);
    }
}