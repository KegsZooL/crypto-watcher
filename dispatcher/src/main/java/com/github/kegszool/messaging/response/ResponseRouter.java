package com.github.kegszool.messaging.response;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.router.AbstractRouter;
import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.router.HandlerNotFoundException;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.exception.ResponseHandlerNotFoundException;

@Log4j2
@Component
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
    protected HandlerNotFoundException processMissingHandler(ServiceMessage<?> serviceMessage, String key) {
        String warnMessage = String.format("No response handler found for routing key: \"%s\". ChatId: \"%s\".",
                                           key, serviceMessage.getChatId());
        log.warn(warnMessage);
        return new ResponseHandlerNotFoundException(warnMessage);
    }
}