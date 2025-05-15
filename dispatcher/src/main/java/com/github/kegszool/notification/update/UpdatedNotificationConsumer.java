package com.github.kegszool.notification.update;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

@Log4j2
@Component
public class UpdatedNotificationConsumer extends BaseResponseConsumer<List<UserNotificationUpdateDto>> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.update_notification_response}")
    public void consume(ServiceMessage<List<UserNotificationUpdateDto>> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<List<UserNotificationUpdateDto>> getTypeReference() {
        return new TypeReference<>() {};
    }

    @Override
    protected void logReceivedData(ServiceMessage<List<UserNotificationUpdateDto>> serviceMessage, String routingKey) {
        log.info("Confirmation of notification updates has been received");
    }
}