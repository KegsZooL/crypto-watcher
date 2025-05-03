package com.github.kegszool.notificaiton.creation;

import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.messaging.producer.ProducerService;

import org.springframework.amqp.support.AmqpHeaders;

@Component
public class CreateNotificationRequestConsumer extends BaseRequestConsumer<NotificationDto, NotificationCreationRequestExecutor> {

    public CreateNotificationRequestConsumer(
            ProducerService responseProducer,
            NotificationCreationRequestExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.create_notification_request}")
    public void listen(
            ServiceMessage<NotificationDto> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}