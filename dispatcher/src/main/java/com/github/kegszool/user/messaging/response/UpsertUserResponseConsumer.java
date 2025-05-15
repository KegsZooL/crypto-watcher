package com.github.kegszool.user.messaging.response;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.user.messaging.dto.UserDto;
import com.github.kegszool.user.messaging.dto.UpsertUserResponse;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

@Log4j2
@Component
public class UpsertUserResponseConsumer extends BaseResponseConsumer<UpsertUserResponse> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.upsert_user_response}")
    public void consume(ServiceMessage<UpsertUserResponse> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<UpsertUserResponse> getTypeReference() {
        return new TypeReference<>(){};
    }

    @Override
    protected void logReceivedData(ServiceMessage<UpsertUserResponse> serviceMessage, String routingKey) {
        UpsertUserResponse response = serviceMessage.getData();
        UserDto user = response.getUserData().getUser();
        Long userId = user.getTelegramId();
        log.info("Upsert user response has been received. User id: {}", userId);
    }
}