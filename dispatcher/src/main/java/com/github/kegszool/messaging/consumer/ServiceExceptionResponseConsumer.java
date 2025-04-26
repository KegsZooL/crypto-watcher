package com.github.kegszool.messaging.consumer;

import com.github.kegszool.messaging.dto.service.ServiceException;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ServiceExceptionResponseConsumer extends BaseResponseConsumer<ServiceException> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.service_exception}")
    public void consume(ServiceMessage<ServiceException> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<ServiceException> getDataClass() {
        return ServiceException.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<ServiceException> serviceMessage, String routingKey) {
        ServiceException serviceException = serviceMessage.getData();
        log.error("Exception \"{}\" was received from the microservice by routing key \"{}\"" +
                  "Exception message: \"{}\"", serviceException.getExceptionName(), routingKey, serviceException.getMessage());
    }
}