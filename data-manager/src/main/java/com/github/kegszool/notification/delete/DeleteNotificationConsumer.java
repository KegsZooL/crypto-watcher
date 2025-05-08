package com.github.kegszool.notification.delete;

import org.springframework.stereotype.Component;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.producer.ProducerService;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.notification.NotificationIdentifierDto;

@Component
public class DeleteNotificationConsumer extends BaseRequestConsumer<NotificationIdentifierDto, DeleteNotificationExecutor> {

    @Autowired
    public DeleteNotificationConsumer(
            ProducerService responseProducer,
            DeleteNotificationExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.delete_notification.request}")
    public void listen(
            ServiceMessage<NotificationIdentifierDto> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}