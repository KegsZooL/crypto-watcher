package com.github.kegszool.service;

public interface RequestProducerService {
    void produce(String rabbitQueue, String request);
}