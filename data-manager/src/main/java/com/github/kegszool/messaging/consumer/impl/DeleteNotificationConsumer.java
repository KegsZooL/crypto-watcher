package com.github.kegszool.messaging.consumer.impl;

import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ProducerService;
import com.github.kegszool.notification.NotificationIdentifierDto;
import com.github.kegszool.request.impl.DeleteNotificationExecutor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class DeleteNotificationConsumer extends BaseRequestConsumer<NotificationIdentifierDto, DeleteNotificationExecutor> {

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