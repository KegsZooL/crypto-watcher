package com.github.kegszool.coin.exists;

import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.consumer.BaseRequestConsumer;
import com.github.kegszool.messaging.dto.command_entity.UserCoinData;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.messaging.producer.ProducerService;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Component
public class CoinExistsConsumer extends BaseRequestConsumer<UserCoinData, CoinExistsExecutor> {

    public CoinExistsConsumer(
            ProducerService responseProducer,
            CoinExistsExecutor executor
    ) {
        super(responseProducer, executor);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.check_coin_exists_request}")
    public void listen(
            ServiceMessage<UserCoinData> serviceMessage,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        super.consume(serviceMessage, routingKey);
    }
}