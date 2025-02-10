package com.github.kegszool.bot.handler.response.database;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.bot.handler.result.HandlerResult;
import com.github.kegszool.messaging.dto.command_entity.UpsertUserResponse;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UpsertUserResponseHandler extends BaseResponseHandler<UpsertUserResponse> {

    @Value("${spring.rabbitmq.template.routing-key.upsert_user_response}")
    private String UPSERT_USER_RESPONSE_ROUTING_KEY;

    @Override
    public boolean canHandle(String routingKey) {
        return UPSERT_USER_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<UpsertUserResponse> serviceMessage) {
        var responseServiceMsg = serviceMessage.getData();
        if(responseServiceMsg.isSuccess()) {

        }
        return new HandlerResult.NoResponse(); // TODO think about passing null value in classes extends from BaseResponseHandler
    }
}