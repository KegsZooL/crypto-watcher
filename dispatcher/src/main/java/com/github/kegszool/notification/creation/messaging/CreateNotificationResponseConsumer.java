package com.github.kegszool.notification.creation.messaging;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.user.messaging.dto.UserData;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

@Log4j2
@Component
public class CreateNotificationResponseConsumer extends BaseResponseConsumer<UserData> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.create_notification_response_from_db}")
    public void consume(ServiceMessage<UserData> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<UserData> getTypeReference() {
        return new TypeReference<>(){};
    }

    @Override
    protected void logReceivedData(ServiceMessage<UserData> serviceMessage, String routingKey) {
        log.info("Confirmation of notification creation has been received for chat id '{}'", serviceMessage.getChatId());
    }
}