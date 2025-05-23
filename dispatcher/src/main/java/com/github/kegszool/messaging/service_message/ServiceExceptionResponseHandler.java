package com.github.kegszool.messaging.service_message;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.service.ServiceException;

import com.github.kegszool.messaging.response.BaseResponseHandler;
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