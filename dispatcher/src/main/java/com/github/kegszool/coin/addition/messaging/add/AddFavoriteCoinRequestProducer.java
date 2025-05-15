package com.github.kegszool.coin.addition.messaging.add;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.coin.dto.UserCoinData;
import com.github.kegszool.user.messaging.dto.UserDto;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.RequestProducerService;

@Service
public class AddFavoriteCoinRequestProducer {

    private final RequestProducerService producerService;
    private final String routingKey;

    @Autowired
    public AddFavoriteCoinRequestProducer(
            RequestProducerService producerService,
            @Value("${spring.rabbitmq.template.routing-key.add_coin_request}") String routingKey
    ) {
        this.producerService = producerService;
        this.routingKey = routingKey;
    }

    public void send(UserDto user, List<String> coins, Integer messageId, String chatId) {
        UserCoinData userCoinData = new UserCoinData(user, coins);
        ServiceMessage<UserCoinData> request = new ServiceMessage<>(messageId, chatId, userCoinData);
        producerService.produce(routingKey,request);
    }
}