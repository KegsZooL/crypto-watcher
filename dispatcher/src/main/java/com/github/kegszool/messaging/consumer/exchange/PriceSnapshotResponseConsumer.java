package com.github.kegszool.messaging.consumer.exchange;

import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.CoinPriceSnapshot;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PriceSnapshotResponseConsumer extends BaseResponseConsumer<CoinPriceSnapshot> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_coin_price}")
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