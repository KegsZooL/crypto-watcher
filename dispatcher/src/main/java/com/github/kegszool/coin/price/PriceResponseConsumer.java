package com.github.kegszool.coin.price;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.consumer.BaseResponseConsumer;

@Log4j2
@Service
public class PriceResponseConsumer extends BaseResponseConsumer<CoinPrice> {

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.coin_price_response}")
    public void consume(ServiceMessage<CoinPrice> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<CoinPrice> getDataClass() {
        return CoinPrice.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<CoinPrice> serviceMessage, String routingKey) {
        CoinPrice receivedSnapshot = serviceMessage.getData();
        String coinName = receivedSnapshot.getName();
        log.info("Received a response to the price snapshot for coin \"{}\"", coinName);
    }
}