package com.github.kegszool.notification.triggered;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

@Log4j2
@Component
public class TriggeredNotificationConsumer extends BaseResponseConsumer<NotificationDto> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.triggered_notification}")
    public void consume(ServiceMessage<NotificationDto> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<NotificationDto> getTypeReference() {
        return new TypeReference<>(){};
    }

    @Override
    protected void logReceivedData(ServiceMessage<NotificationDto> serviceMessage, String routingKey) {
        log.info("Triggered notification for the coin '{}' has been received | Chat id: {}",
                serviceMessage.getData().getCoin().getName(), serviceMessage.getChatId());
    }
}