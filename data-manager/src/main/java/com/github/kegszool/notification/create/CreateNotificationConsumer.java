package com.github.kegszool.notification.create;

import org.springframework.stereotype.Component;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;

import com.github.kegszool.messaging.producer.ProducerService;
import com.github.kegszool.messaging.consumer.BaseRequestConsumer;

@Component
public class CreateNotificationConsumer extends BaseRequestConsumer<NotificationDto, CreateNotificationExecutor> {

    @Autowired
    public CreateNotificationConsumer(
            ProducerService responseProducer,
            CreateNotificationExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.create_notification.request}")
    public void listen(
            ServiceMessage<NotificationDto> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}