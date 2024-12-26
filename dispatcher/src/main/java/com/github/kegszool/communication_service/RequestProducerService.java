package com.github.kegszool.communication_service;

public interface RequestProducerService {
    void produce(String rabbitQueue, String request);
}