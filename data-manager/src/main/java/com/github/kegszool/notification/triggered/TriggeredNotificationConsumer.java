package com.github.kegszool.notification.triggered;

import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.messaging.producer.ProducerService;
import com.github.kegszool.messaging.consumer.BaseRequestConsumer;

import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Component
public class TriggeredNotificationConsumer extends BaseRequestConsumer<NotificationDto, TriggeredNotificationExecutor> {

    @Autowired
    public TriggeredNotificationConsumer(
            ProducerService responseProducer,
            TriggeredNotificationExecutor executor
    ) {
     super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.after_triggered_notification.request}")
    public void listen(
            ServiceMessage<NotificationDto> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
     super.consume(serviceMessage, routingKey);
    }
}