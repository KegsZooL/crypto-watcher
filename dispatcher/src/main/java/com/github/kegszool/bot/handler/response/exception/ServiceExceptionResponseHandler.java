package com.github.kegszool.bot.handler.response.exception;

import com.github.kegszool.bot.handler.response.BaseResponseHandler;
import com.github.kegszool.bot.handler.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceException;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Log4j2
@Component
public class ServiceExceptionResponseHandler extends BaseResponseHandler<ServiceException> {

    @Value("${menu.answer_message.service_exception}")
    private String ANSWER_TEXT_MESSAGE;

    @Value("${spring.rabbitmq.template.routing-key.service_exception}")
    private String SERVICE_EXCEPTION_ROUTING_KEY;

    @Override
    public boolean canHandle(String routingKey) {
        return SERVICE_EXCEPTION_ROUTING_KEY.equals(routingKey);
    }

    // TODO add logic for handling service exception with filtering by type
    @Override
    public HandlerResult handle(ServiceMessage<ServiceException> serviceMessage) {
        handleServiceException(serviceMessage.getData());
        String chatId = serviceMessage.getChatId();
        SendMessage answerMessage = new SendMessage(chatId, ANSWER_TEXT_MESSAGE);
        return new HandlerResult.Success(answerMessage);
    }

    private void handleServiceException(ServiceException serviceException) {
        String exceptionName = serviceException.getExceptionName();
        String exceptionMsg = serviceException.getMessage();
        var logMsgPattern = "Service exception occurred. Exception type: {}. Exception msg: {}";
        log.warn(logMsgPattern, exceptionName, exceptionMsg);
    }
}