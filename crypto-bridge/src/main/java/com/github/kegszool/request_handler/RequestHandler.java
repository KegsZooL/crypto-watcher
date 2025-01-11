package com.github.kegszool.request_handler;

import com.github.kegszool.communication_service.ResponseProducerService;

public interface RequestHandler {
    boolean canHandle(String request);
    void handle(String request, ResponseProducerService responseProducerService);
}
