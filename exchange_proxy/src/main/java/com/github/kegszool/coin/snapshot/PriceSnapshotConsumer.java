package com.github.kegszool.coin.snapshot;

import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ServiceMessageProducer;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Header;

@Service
public class PriceSnapshotConsumer extends BaseRequestConsumer<String, PriceSnapshotExecutor> {

    public PriceSnapshotConsumer(
            ServiceMessageProducer producer,
            PriceSnapshotExecutor executor
    ) {
        super(producer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.coin_price_request}")
    public void listen(
            ServiceMessage<String> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}