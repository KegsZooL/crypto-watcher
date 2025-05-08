package com.github.kegszool.coin.delete;

import org.springframework.stereotype.Component;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.producer.ProducerService;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.consumer.BaseRequestConsumer;

@Component
public class DeleteFavoriteCoinConsumer extends BaseRequestConsumer<UserData, DeleteFavoriteCoinExecutor> {

    public DeleteFavoriteCoinConsumer(
            ProducerService responseProducer,
            DeleteFavoriteCoinExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.delete_favorite_coin.request}")
    public void listen(
            ServiceMessage<UserData> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}