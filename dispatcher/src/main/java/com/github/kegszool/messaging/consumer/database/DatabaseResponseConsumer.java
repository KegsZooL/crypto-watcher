package com.github.kegszool.messaging.consumer.database;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Component
public abstract class DatabaseResponseConsumer<T> extends BaseResponseConsumer<T> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_from_database}")
    public void consume(ServiceMessage<T> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }
}