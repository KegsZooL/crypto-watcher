package com.github.kegszool.messaging.consumer.impl;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.messaging.producer.ResponseProducerService;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.ChangeUserLanguageRequest;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.request_executor.impl.ChangeLanguageExecutor;

@Component
public class ChangeLanguageConsumer extends BaseRequestConsumer<ChangeUserLanguageRequest, ChangeLanguageExecutor> {

    @Autowired
    public ChangeLanguageConsumer(
            ResponseProducerService responseProducer,
            ChangeLanguageExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.change_language.request}")
    public void listen(
            ServiceMessage<ChangeUserLanguageRequest> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}