package com.github.kegszool.messaging.consumer.database.impl;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;
import com.github.kegszool.messaging.consumer.database.DatabaseResponseConsumer;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class UpsertUserDatabaseResponseConsumer extends DatabaseResponseConsumer<UpsertUserResponse> {

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_response}")
    private String UPSERT_USER_RESPONSE_ROUTING_KEY;

    @Override
    protected boolean canHandle(String routingKey) {
        return UPSERT_USER_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public void consume(ServiceMessage<UpsertUserResponse> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<UpsertUserResponse> getDataClass() {
        return UpsertUserResponse.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<UpsertUserResponse> serviceMessage, String routingKey) {
        //TODO add logging
    }
}