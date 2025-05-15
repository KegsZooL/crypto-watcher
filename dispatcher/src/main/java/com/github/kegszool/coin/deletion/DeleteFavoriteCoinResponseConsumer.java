package com.github.kegszool.coin.deletion;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.user.messaging.dto.UserDto;
import com.github.kegszool.user.messaging.dto.UserData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

@Log4j2
@Component
public class DeleteFavoriteCoinResponseConsumer extends BaseResponseConsumer<UserData> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.delete_favorite_coin_response}")
    public void consume(ServiceMessage<UserData> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<UserData> getTypeReference() {
        return new TypeReference<>(){};
    }

    @Override
    protected void logReceivedData(ServiceMessage<UserData> serviceMessage, String routingKey) {
        UserData userData = serviceMessage.getData();
        UserDto user = userData.getUser();
        Long userId = user.getTelegramId();
        log.info("The user's coins have been successfully deleted. User id: {}", userId);
    }
}