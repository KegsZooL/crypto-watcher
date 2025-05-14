package com.github.kegszool.coin.price.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kegszool.coin.price.model.PriceSnapshot;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

@Log4j2
@Service
public class PriceResponseConsumer extends BaseResponseConsumer<PriceSnapshot> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.coin_price_response}")
    public void consume(ServiceMessage<PriceSnapshot> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<PriceSnapshot> getTypeReference() {
        return new TypeReference<>(){};
    }

    @Override
    protected void logReceivedData(ServiceMessage<PriceSnapshot> serviceMessage, String routingKey) {
        PriceSnapshot receivedSnapshot = serviceMessage.getData();
        String coinName = receivedSnapshot.getName();
        log.info("Received a response to the price snapshot for coin \"{}\"", coinName);
    }
}