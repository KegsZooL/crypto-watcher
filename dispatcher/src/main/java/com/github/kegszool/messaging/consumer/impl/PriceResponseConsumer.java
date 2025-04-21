package com.github.kegszool.messaging.consumer.impl;

import com.github.kegszool.coin.dto.CoinPrice;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.ServiceMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PriceResponseConsumer extends BaseResponseConsumer<CoinPrice> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_coin_price}")
    public void consume(ServiceMessage<CoinPrice> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<CoinPrice> getDataClass() {
        return CoinPrice.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<CoinPrice> serviceMessage, String routingKey) {
        CoinPrice receivedSnapshot = serviceMessage.getData();
        String coinName = receivedSnapshot.getName();
        log.info("Received a response to the price snapshot for coin \"{}\"", coinName);
    }
}