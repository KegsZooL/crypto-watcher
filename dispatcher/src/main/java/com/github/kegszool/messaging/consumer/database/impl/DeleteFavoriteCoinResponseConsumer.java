package com.github.kegszool.messaging.consumer.database.impl;

import com.github.kegszool.messaging.consumer.database.DatabaseResponseConsumer;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DeleteFavoriteCoinResponseConsumer extends DatabaseResponseConsumer<UserData> {

    @Value("${spring.rabbitmq.template.routing-key.delete_favorite_coin_response}")
    private String DELETE_FAVORITE_COIN_RESPONSE_ROUTING_KEY;

    @Override
    protected boolean canHandle(String routingKey) {
        return DELETE_FAVORITE_COIN_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public void consume(ServiceMessage<UserData> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<UserData> getDataClass() {
        return UserData.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<UserData> serviceMessage, String routingKey) {
        UserData userData = serviceMessage.getData();
        UserDto user = userData.getUser();
        Long userId = user.getTelegramId();
        log.info("The user's coins have been successfully deleted. User id: {}", userId);
    }
}