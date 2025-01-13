package com.github.kegszool.communication_service;

import com.github.kegszool.DTO.DataTransferObject;

public interface ResponseProducerService {
    void produce(DataTransferObject dataTransferObject, String routingKey);
}