package com.github.kegszool.messaging.consumer.impl;

import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.user.dto.UserDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DeleteFavoriteCoinResponseConsumer extends BaseResponseConsumer<UserData> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_delete_favorite_coin}")
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