package com.github.kegszool.messaging.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.service.ServiceException;

@Log4j2
@Service
public class ServiceExceptionResponseConsumer extends BaseResponseConsumer<ServiceException> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.service_exception}")
    public void consume(ServiceMessage<ServiceException> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<ServiceException> getTypeReference() {
        return new TypeReference<>(){};
    }

    @Override
    protected void logReceivedData(ServiceMessage<ServiceException> serviceMessage, String routingKey) {
        ServiceException serviceException = serviceMessage.getData();
        log.error("Exception \"{}\" was received from the microservice by routing key \"{}\"" +
                  "Exception message: \"{}\"", serviceException.getExceptionName(), routingKey, serviceException.getMessage());
    }
}