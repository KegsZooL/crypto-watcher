package com.github.kegszool.communication_service;

import com.github.kegszool.DTO.DataTransferObject;

public interface RequestProducerService {
    void produce(String rabbitQueue, DataTransferObject dataTransferObject);
}