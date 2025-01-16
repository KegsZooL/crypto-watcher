package com.github.kegszool.messaging.consumer;

import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.bot.controll.TelegramBotController;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ExchangeResponseConsumer implements ResponseConsumerService {

    private final TelegramBotController botController;

    @Autowired
    public ExchangeResponseConsumer(TelegramBotController botController) {
        this.botController = botController;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_from_exchange_queue}")
    public void consume(ServiceMessage serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        if(isDataValid(serviceMessage, routingKey)) {
           log.info("A response was received from the exchange:\n\t\t{}\n",
                   serviceMessage.getData());
           botController.handleResponse(serviceMessage, routingKey);
        }
        //TODO: обработать данное исключение
    }

    private boolean isDataValid(ServiceMessage serviceMessage, String routingKey) {
        if(serviceMessage == null || serviceMessage.getData() == null || routingKey == null) {
            return false;
        }
        return true;
    }
}