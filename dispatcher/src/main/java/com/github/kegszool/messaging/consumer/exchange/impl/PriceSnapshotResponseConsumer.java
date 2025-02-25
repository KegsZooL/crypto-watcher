package com.github.kegszool.messaging.consumer.exchange.impl;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.CoinPriceSnapshot;
import com.github.kegszool.messaging.consumer.exchange.ExchangeResponseConsumer;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PriceSnapshotResponseConsumer extends ExchangeResponseConsumer<CoinPriceSnapshot> {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Override
    protected boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public void consume(ServiceMessage<CoinPriceSnapshot> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<CoinPriceSnapshot> getDataClass() {
        return CoinPriceSnapshot.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<CoinPriceSnapshot> serviceMessage, String routingKey) {
        CoinPriceSnapshot receivedSnapshot = serviceMessage.getData();
        String coinName = receivedSnapshot.getName();
        log.info("Received a response to the price snapshot for coin \"{}\"", coinName);
    }
}