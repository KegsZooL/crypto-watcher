package com.github.kegszool.messaging.consumer.database;

import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class UpsertUserResponseConsumer extends BaseResponseConsumer<UpsertUserResponse> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_upsert_user}")
    public void consume(ServiceMessage<UpsertUserResponse> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<UpsertUserResponse> getDataClass() {
        return UpsertUserResponse.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<UpsertUserResponse> serviceMessage, String routingKey) {
        UpsertUserResponse response = serviceMessage.getData();
        UserDto user = response.getUserData().getUser();
        Long userId = user.getTelegramId();
        log.info("Upsert user response has been received. User id: {}", userId);
    }
}