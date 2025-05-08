package com.github.kegszool.coin.add;

import org.springframework.stereotype.Component;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.UserCoinData;

import com.github.kegszool.messaging.producer.ProducerService;
import com.github.kegszool.messaging.consumer.BaseRequestConsumer;

@Component
public class AddFavoriteCoinConsumer extends BaseRequestConsumer<UserCoinData, AddFavoriteCoinExecutor> {

    @Autowired
    public AddFavoriteCoinConsumer(
            ProducerService responseProducer,
            AddFavoriteCoinExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.add_favorite_coin.request}")
    public void listen(
            ServiceMessage<UserCoinData> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}