package com.github.kegszool.notificaiton.active;

import com.github.kegszool.messaging.dto.NotificationDto;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.consumer.BaseRequestConsumer;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ProducerService;

import java.util.List;

@Component
public class ActiveNotificationConsumer extends BaseRequestConsumer<List<NotificationDto>, ActiveNotificationResponseExecutor> {

    public ActiveNotificationConsumer(
            ProducerService responseProducer,
            ActiveNotificationResponseExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.get_active_notification_response}")
    public void listen(
            ServiceMessage<List<NotificationDto>> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}