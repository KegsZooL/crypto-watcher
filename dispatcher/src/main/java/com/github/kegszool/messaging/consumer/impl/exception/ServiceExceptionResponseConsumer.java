package com.github.kegszool.messaging.consumer.impl.exception;

import com.github.kegszool.bot.controll.TelegramBotController;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.service.ServiceException;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
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
    @RabbitListener(queues = {
            "${spring.rabbitmq.queues.response_from_exchange}",
            "${spring.rabbitmq.queues.response_from_database}"
    })
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