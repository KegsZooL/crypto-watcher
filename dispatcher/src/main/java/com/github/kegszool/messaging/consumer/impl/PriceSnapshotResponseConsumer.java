package com.github.kegszool.messaging.consumer.impl;

import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.CoinPriceSnapshot;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.bot.controll.TelegramBotController;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Log4j2
public class PriceSnapshotResponseConsumer extends BaseResponseConsumer<CoinPriceSnapshot> {

    @Value("${spring.rabbitmq.template.routing-key.coin_price_response}")
    private String COIN_PRICE_RESPONSE_ROUTING_KEY;

    @Autowired
    public PriceSnapshotResponseConsumer(TelegramBotController botController) {
        super(botController);
    }

    @Override
    protected boolean canHandle(String routingKey) {
        return COIN_PRICE_RESPONSE_ROUTING_KEY.equals(routingKey);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.response_from_exchange}")
    public void consume(ServiceMessage<CoinPriceSnapshot> serviceMessage, String routingKey) {
        super.consume(serviceMessage, routingKey);
    }

    @Override
    protected Class<CoinPriceSnapshot> getDataClass() {
        return CoinPriceSnapshot.class;
    }

    @Override
    protected void logReceivedData(ServiceMessage<CoinPriceSnapshot> serviceMessage, String routingKey) {
        CoinPriceSnapshot receivedSnapshot = serviceMessage.getData();
        String coinName = receivedSnapshot.getName();
        log.info("Received a response to the price snapshot for coin:\n\t\t{}\n", coinName);
    }
}