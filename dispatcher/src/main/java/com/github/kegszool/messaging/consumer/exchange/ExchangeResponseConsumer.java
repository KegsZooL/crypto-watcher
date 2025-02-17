package com.github.kegszool.messaging.consumer.exchange;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Component
public abstract class ExchangeResponseConsumer<T> extends BaseResponseConsumer<T> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_from_exchange}")
    public void consume(ServiceMessage<T> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }
}