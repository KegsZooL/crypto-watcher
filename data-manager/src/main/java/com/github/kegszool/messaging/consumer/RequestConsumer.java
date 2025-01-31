package com.github.kegszool.messaging.consumer;

import com.github.kegszool.database.entity.dto.BaseEntityDto;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class RequestConsumer implements RequestConsumerService {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.request_to_database}")
    public void consume(ServiceMessage<BaseEntityDto> serviceMessage, String routingKey) {

    }
}