package com.github.kegszool.messaging.producer;

import com.github.kegszool.messaging.dto.ServiceMessage;

public interface RequestProducerService {
    void produce(String rabbitQueue, ServiceMessage serviceMessage);
}