package com.github.kegszool.bot.router.impl;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.bot.router.AbstractRouter;
import com.github.kegszool.exception.handler.HandlerNotFoundException;
import com.github.kegszool.exception.handler.impl.ResponseHandlerNotFoundException;
import com.github.kegszool.bot.handler.response.ResponseHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import java.util.List;

@Component
@Log4j2
public class ResponseRouter extends AbstractRouter<ServiceMessage<?>, ResponseHandler> {

    @Autowired
    public ResponseRouter(List<ResponseHandler> handlers) {
        super(handlers);
    }

    @Override
    protected HandlerNotFoundException proccessMissingHandler(ServiceMessage<?> serviceMessage, Object key) {
        String routingKey = (String) key;
        String warnMessage = String.format(
                "No response handler found for routing key: \"%s\". ChatId: \"%s\".",
                routingKey, serviceMessage.getChatId()
        );
        log.warn(warnMessage);
        return new ResponseHandlerNotFoundException(warnMessage);
    }

    @Override
    protected PartialBotApiMethod<?> handle(ResponseHandler handler, ServiceMessage<?> data) {
        return handler.handle(data);
    }

    @Override
    protected boolean canHandle(ResponseHandler handler, Object key) {
        return handler.canHandle((String)key);
    }
}