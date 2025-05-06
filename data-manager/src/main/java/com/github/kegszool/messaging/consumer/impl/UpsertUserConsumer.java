package com.github.kegszool.messaging.consumer.impl;

import com.github.kegszool.messaging.dto.database_entity.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.messaging.producer.ProducerService;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.request.impl.UpsertUserExecutor;

@Component
public class UpsertUserConsumer extends BaseRequestConsumer<UserDto, UpsertUserExecutor> {

    public UpsertUserConsumer(
            ProducerService responseProducer,
            UpsertUserExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.upsert_user.request}")
    public void listen(
            ServiceMessage<UserDto> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}