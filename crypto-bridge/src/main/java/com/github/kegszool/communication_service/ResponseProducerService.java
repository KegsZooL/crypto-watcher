package com.github.kegszool.communication_service;

public interface ResponseProducerService {
    void produce(String response, String routingKey);
}