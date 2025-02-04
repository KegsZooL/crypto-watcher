package com.github.kegszool.messaging.consumer.impl.exchange;

import com.github.kegszool.bot.controll.TelegramBotController;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public abstract class ExchangeResponseConsumer<T> extends BaseResponseConsumer<T> {

    public ExchangeResponseConsumer(TelegramBotController botController) {
        super(botController);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_from_exchange}")
    public void consume(ServiceMessage<T> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }
}