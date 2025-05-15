package com.github.kegszool.coin.addition.messaging.check_exists;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.coin.dto.UserCoinData;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;

@Component
public class CheckCoinExistsRequestProducer {

    private final String routingKey;
    private final RequestProducerService producer;

    public CheckCoinExistsRequestProducer(
            @Value("${spring.rabbitmq.template.routing-key.check_coin_exists_request}") String routingKey,
            RequestProducerService producer
    ) {
        this.routingKey = routingKey;
        this.producer = producer;
    }

    public void send(UserCoinData userCoinData, Message message) {
        ServiceMessage<UserCoinData> serviceMsg = new ServiceMessage<>(
                message.getMessageId(),
                message.getChatId().toString(),
                userCoinData
        );
        producer.produce(routingKey, serviceMsg);
    }
}