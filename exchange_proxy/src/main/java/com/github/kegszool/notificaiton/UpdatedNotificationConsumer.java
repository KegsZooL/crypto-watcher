package com.github.kegszool.notificaiton;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.messaging.dto.NotificationDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.notificaiton.update.NotificationUpdateConfirmationHandler;

@Component
public class UpdatedNotificationConsumer {

    private final NotificationUpdateConfirmationHandler confirmationHandler;

    @Autowired
    public UpdatedNotificationConsumer(NotificationUpdateConfirmationHandler confirmationHandler) {
        this.confirmationHandler = confirmationHandler;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.notification_updated}")
    public void listen(
            ServiceMessage<List<NotificationDto>> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        confirmationHandler.handle(serviceMessage.getData());
    }
}