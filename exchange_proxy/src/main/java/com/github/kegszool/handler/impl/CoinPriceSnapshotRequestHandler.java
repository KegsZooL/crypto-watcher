package com.github.kegszool.handler.impl;

import com.github.kegszool.exception.json.InvalidJsonFormatException;
import com.github.kegszool.exception.json.JsonFieldNotFoundException;
import com.github.kegszool.exception.request.price_snapshot.CoinPriceSnapshotRequestException;
import com.github.kegszool.handler.BaseRequestHandler;
import com.github.kegszool.messaging.dto.CoinPriceSnapshot;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.controller.RestCryptoController;
import com.github.kegszool.utils.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;


@Component
@Log4j2
public class CoinPriceSnapshotRequestHandler extends BaseRequestHandler {

    //TODO: переписать

    @Value("${api.exchange.url.market.ticker.base}")
    private String BASE_REQUEST_URL;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Value("${spring.rabbitmq.template.routing-key.coin_price_request}")
    private String COIN_PRICE_REQUEST_ROUTING_KEY;

    @Value("${api.exchange.url.market.ticker.response_parameter.last_price}")
    private String LAST_PRICE_FIELD_NAME;

    public CoinPriceSnapshotRequestHandler(
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

        String coin = serviceMessage.getData();
        String requestUrl = BASE_REQUEST_URL + coin;

        ServiceMessage<CoinPriceSnapshot> responseServiceMessage = new ServiceMessage<>();

        try {
            String response = restCryptoController.getResponse(requestUrl);

            String lastPriceStr = jsonParser.parse(response, LAST_PRICE_FIELD_NAME);
            double lastPrice = tryParseDouble(lastPriceStr);

            var snapshost = new CoinPriceSnapshot();
            snapshost.setLastPrice(lastPrice);
            snapshost.setName(coin);

            responseServiceMessage.setData(snapshost);
            responseServiceMessage.setChatId(serviceMessage.getChatId());

        } catch (RestClientException | JsonFieldNotFoundException | InvalidJsonFormatException ex ) {
            throw processErrorOfReceivingPrice(requestUrl, ex);
        }
        return responseServiceMessage;
    }

    private double tryParseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch(NullPointerException ex) {
            return -1;
        } catch(NumberFormatException ex) {
            return -1;
        }
    }

    private CoinPriceSnapshotRequestException processErrorOfReceivingPrice(String requestUrl, Exception ex) {
        log.error("Error executing the GET request to receive the coin price.\n\t\t Request: {}", requestUrl, ex);
        return new CoinPriceSnapshotRequestException("Error executing the GET request: " + requestUrl, ex);
    }
}