package com.github.kegszool.notification.update;

import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

@Component
public class UpdatedNotificationConsumer extends BaseResponseConsumer<List<UserData>> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.update_notification_response}")
    public void consume(ServiceMessage<List<UserData>> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<List<UserData>> getTypeReference() {
        return new TypeReference<>() {};
    }

    //TODO: dummy
    @Override
    protected void logReceivedData(ServiceMessage<List<UserData>> serviceMessage, String routingKey) {
    }
}