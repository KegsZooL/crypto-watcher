package com.github.kegszool.coin.addition.messaging.check_exists.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kegszool.coin.dto.CoinExistenceResult;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CoinExistsResponseConsumer extends BaseResponseConsumer<CoinExistenceResult> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.check_coin_exists_response}")
    public void consume(ServiceMessage<CoinExistenceResult> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected TypeReference<CoinExistenceResult> getTypeReference() {
        return new TypeReference<>(){};
    }

    @Override
    protected void logReceivedData(ServiceMessage<CoinExistenceResult> serviceMessage, String routingKey) {
        //TODO: write logging
    }
}