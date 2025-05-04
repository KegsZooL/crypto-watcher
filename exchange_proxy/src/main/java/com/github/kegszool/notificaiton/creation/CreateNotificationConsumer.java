package com.github.kegszool.notificaiton.creation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.messaging.producer.ProducerService;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.notificaiton.NotificationProducer;

@Component
public class CreateNotificationConsumer extends BaseRequestConsumer<NotificationDto, NotificationCreationExecutor> {

    private final NotificationProducer notificationProducer;

    @Autowired
    public CreateNotificationConsumer(
            ProducerService responseProducer,
            NotificationCreationExecutor executor,
            NotificationProducer notificationProducer
    ) {
        super(responseProducer, executor);
        this.notificationProducer = notificationProducer;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.create_notification_request}")
    public void listen(
            ServiceMessage<NotificationDto> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.notification_created}")
    public void listenCreated(
            ServiceMessage<String> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        notificationProducer.sendUploadRequestForActive(serviceMessage.getData(), serviceMessage);
    }
}