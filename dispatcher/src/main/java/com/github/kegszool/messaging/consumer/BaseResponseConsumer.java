package com.github.kegszool.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kegszool.bot.controll.TelegramBotController;
import com.github.kegszool.messaging.dto.ServiceMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public abstract class BaseResponseConsumer<T> implements ResponseConsumerService<T> {

    protected final TelegramBotController botController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BaseResponseConsumer(TelegramBotController botController) {
        this.botController = botController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_from_exchange}")
    public void consume(ServiceMessage<T> serviceMessage, String routingKey) {
        if(isDataValid(serviceMessage, routingKey)) {
            ServiceMessage<T> mappedMessage = mapToObject(serviceMessage);
            handleResponse(mappedMessage, routingKey);
        }
        handleException();
    }

    private boolean  isDataValid(ServiceMessage<?> serviceMessage, String routingKey) {
        return serviceMessage != null || serviceMessage.getData() != null || routingKey == null;
    }

    private ServiceMessage<T> mapToObject(ServiceMessage<?> serviceMessage) {
        try {
            T data = objectMapper.convertValue(serviceMessage.getData(), getDataClass());
            ServiceMessage<T> mappedMessage = new ServiceMessage<>();
            mappedMessage.setChatId(serviceMessage.getChatId());
            mappedMessage.setData(data);
            return mappedMessage;
        }
        catch (Exception ex) {
          throw new RuntimeException(ex); //TODO: create custom exception
        }
    }

    protected abstract Class<T> getDataClass();

    protected abstract void handleResponse(ServiceMessage<T> serviceMessage, String routingKey);

    private void handleException() {}
}