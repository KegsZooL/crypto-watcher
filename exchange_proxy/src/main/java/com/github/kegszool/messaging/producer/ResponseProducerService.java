package com.github.kegszool.messaging.producer;

import com.github.kegszool.messaging.dto.ServiceMessage;

public interface ResponseProducerService {
    void produce(ServiceMessage serviceMessage, String routingKey);
}