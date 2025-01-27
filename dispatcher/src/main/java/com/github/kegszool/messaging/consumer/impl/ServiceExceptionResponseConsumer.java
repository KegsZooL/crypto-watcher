package com.github.kegszool.messaging.consumer.impl;

import com.github.kegszool.bot.controll.TelegramBotController;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.ServiceException;
import com.github.kegszool.messaging.dto.ServiceMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceExceptionResponseConsumer extends BaseResponseConsumer<ServiceException> {

    @Value("${spring.rabbitmq.template.routing-key.service_exception}")
    private String SERVICE_EXCEPTION_ROUTING_KEY;

    @Autowired
    public ServiceExceptionResponseConsumer(TelegramBotController botController) {
        super(botController);
    }

    @Override
    protected boolean canHandle(String routingKey) {
        return SERVICE_EXCEPTION_ROUTING_KEY.equals(routingKey);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_from_exchange}")
    public void consume(ServiceMessage<ServiceException> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<ServiceException> getDataClass() {
        return ServiceException.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<ServiceException> serviceMessage, String routingKey) {

    }
}