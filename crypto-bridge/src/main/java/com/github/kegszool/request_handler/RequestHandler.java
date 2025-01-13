package com.github.kegszool.request_handler;

import com.github.kegszool.DTO.DataTransferObject;
import com.github.kegszool.communication_service.ResponseProducerService;

public interface RequestHandler {
    boolean canHandle(String routingKey);
    void handle(DataTransferObject dataTransferObject, ResponseProducerService responseProducerService);
}
