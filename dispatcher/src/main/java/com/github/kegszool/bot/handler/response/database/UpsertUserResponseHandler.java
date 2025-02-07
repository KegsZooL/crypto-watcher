package com.github.kegszool.bot.handler.response.database;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class UpsertUserResponseHandler extends BaseResponseHandler<UpsertUserResponse> {

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_response}")
    private String UPSERT_USER_RESPONSE_ROUTING_KEY;

    @Override
    public boolean canHandle(String routingKey) {
        return UPSERT_USER_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public PartialBotApiMethod<?> handle(ServiceMessage<UpsertUserResponse> serviceMessage) {
        var response = serviceMessage.getData();
        if(response.isSuccess()) {

        }
        return null; //TODO: think about passing null value in classes extends from BaseResponseHandler
    }
}