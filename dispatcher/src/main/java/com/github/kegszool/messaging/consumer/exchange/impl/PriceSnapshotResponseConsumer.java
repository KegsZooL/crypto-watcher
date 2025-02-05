package com.github.kegszool.messaging.consumer.exchange.impl;

import com.github.kegszool.messaging.consumer.exchange.ExchangeResponseConsumer;
import com.github.kegszool.messaging.dto.command_entity.PriceSnapshot;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PriceSnapshotResponseConsumer extends ExchangeResponseConsumer<PriceSnapshot> {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Override
    protected boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public void consume(ServiceMessage<PriceSnapshot> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<PriceSnapshot> getDataClass() {
        return PriceSnapshot.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<PriceSnapshot> serviceMessage, String routingKey) {
        PriceSnapshot receivedSnapshot = serviceMessage.getData();
        String coinName = receivedSnapshot.getName();
        log.info("Received a response to the price snapshot for coin \"{}\"", coinName);
    }
}