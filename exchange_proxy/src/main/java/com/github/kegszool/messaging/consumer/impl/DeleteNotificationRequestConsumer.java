package com.github.kegszool.messaging.consumer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.messaging.producer.ResponseProducerService;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.request.impl.notificaiton.DeleteNotificationRequestExecutor;

@Component
public class DeleteNotificationRequestConsumer extends BaseRequestConsumer<NotificationDto, DeleteNotificationRequestExecutor> {

    @Autowired
    public DeleteNotificationRequestConsumer(
            ResponseProducerService responseProducer,
            DeleteNotificationRequestExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.delete_notification_request}")
    public void listen(
            ServiceMessage<NotificationDto> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}