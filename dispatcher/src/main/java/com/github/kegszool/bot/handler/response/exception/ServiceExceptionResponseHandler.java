package com.github.kegszool.bot.handler.response.exception;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.bot.handler.result.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ServiceExceptionResponseHandler extends BaseResponseHandler {

    @Value("${spring.rabbitmq.template.routing-key.service_exception}")
    private String SERVICE_EXCEPTION_ROUTING_KEY;

    @Override
    public boolean canHandle(String routingKey) {
        return SERVICE_EXCEPTION_ROUTING_KEY.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage serviceMessage) {
        var answerMessage = new SendMessage(serviceMessage.getChatId(), "Service Exception"); //TODO think about exception handling
        return new HandlerResult.Success(answerMessage);
    }
}