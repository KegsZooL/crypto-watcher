package com.github.kegszool.bot.router.impl;

import com.github.kegszool.messaging.dto.service.ServiceMessage;

import com.github.kegszool.bot.router.AbstractRouter;
import com.github.kegszool.bot.handler.result.HandlerResult;
import com.github.kegszool.bot.handler.response.ResponseHandler;

import com.github.kegszool.exception.bot.handler.HandlerNotFoundException;
import com.github.kegszool.exception.bot.handler.impl.ResponseHandlerNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import java.util.List;

@Component
@Log4j2
public class ResponseRouter extends AbstractRouter<ServiceMessage<?>, ResponseHandler, String> {

    @Autowired
    public ResponseRouter(List<ResponseHandler> handlers) {
        super(handlers);
    }

    @Override
    protected boolean canHandle(ResponseHandler handler, String key) {
        return handler.canHandle(key);
    }

    @Override
    protected HandlerResult handle(ResponseHandler handler, ServiceMessage<?> data) {
        return handler.handle(data);
    }

    @Override
    protected HandlerNotFoundException proccessMissingHandler(ServiceMessage<?> serviceMessage, String key) {
        String warnMessage = String.format("No response handler found for routing key: \"%s\". ChatId: \"%s\".",
                                           key, serviceMessage.getChatId());
        log.warn(warnMessage);
        return new ResponseHandlerNotFoundException(warnMessage);
    }
}