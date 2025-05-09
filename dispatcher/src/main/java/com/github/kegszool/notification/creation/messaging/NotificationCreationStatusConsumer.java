package com.github.kegszool.notification.creation.messaging;

import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationCreationStatusConsumer extends BaseResponseConsumer<Boolean> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.create_notification_response_from_exchange}")
    public void consume(ServiceMessage<Boolean> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<Boolean> getDataClass() {
        return Boolean.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<Boolean> serviceMessage, String routingKey) {
        //TODO: dummy
    }
}