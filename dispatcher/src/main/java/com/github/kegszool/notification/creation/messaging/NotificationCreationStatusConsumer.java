package com.github.kegszool.notification.creation.messaging;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Log4j2
@Component
public class NotificationCreationStatusConsumer extends BaseResponseConsumer<Boolean> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.create_notification_response_from_exchange}")
    public void consume(ServiceMessage<Boolean> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<Boolean> getTypeReference() {
        return new TypeReference<>(){};
    }

    @Override
    protected void logReceivedData(ServiceMessage<Boolean> serviceMessage, String routingKey) {
        log.info("Confirmation of notification creation status has been received for chat id '{}' | Status: {}",
                serviceMessage.getChatId(), serviceMessage.getData());
    }
}