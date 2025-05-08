package com.github.kegszool.notificaiton.subscription;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Component
public class NotificationWebSocketSubscriptionConsumer {

    private final NotificationWebSocketSubscriber notificationSubscriber;

    @Autowired
    public NotificationWebSocketSubscriptionConsumer(NotificationWebSocketSubscriber notificationSubscriber) {
        this.notificationSubscriber = notificationSubscriber;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.notification_websocket_subscription}")
    public void listenSubscribe(
            ServiceMessage<List<NotificationDto>> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        notificationSubscriber.subscribe(serviceMessage.getData());
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.notification_websocket_unsubscription}")
    public void listenUnsubscribe(
            ServiceMessage<NotificationDto> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        notificationSubscriber.unsubscribe(serviceMessage.getData());
    }
}