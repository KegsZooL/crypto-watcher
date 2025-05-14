package com.github.kegszool.notification.triggered;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

@Component
public class TriggeredNotificationConsumer extends BaseResponseConsumer<NotificationDto> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.triggered_notification}")
    public void consume(ServiceMessage<NotificationDto> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<NotificationDto> getDataClass() {
        return NotificationDto.class;
    }

    //TODO: dummy
    @Override
    protected void logReceivedData(ServiceMessage<NotificationDto> serviceMessage, String routingKey) {
    }
}