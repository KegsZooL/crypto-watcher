package com.github.kegszool.messaging.consumer.impl;

import com.github.kegszool.messaging.consumer.BaseResponseConsumer;
import com.github.kegszool.messaging.dto.CoinPriceSnapshot;
import com.github.kegszool.messaging.dto.ServiceMessage;
import com.github.kegszool.bot.controll.TelegramBotController;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Log4j2
public class PriceSnapshotResponseConsumer extends BaseResponseConsumer<CoinPriceSnapshot> {

    @Autowired
    public PriceSnapshotResponseConsumer(TelegramBotController botController) {
        super(botController);
    }

    @Override
    protected Class<CoinPriceSnapshot> getDataClass() {
        return CoinPriceSnapshot.class;
    }

    @Override
    protected void handleResponse(ServiceMessage<CoinPriceSnapshot> serviceMessage, String routingKey) {
        logReceivedData(serviceMessage, routingKey);
        botController.handleResponse(serviceMessage, routingKey);
    }

    private void logReceivedData(ServiceMessage<CoinPriceSnapshot> serviceMessage, String routingKey) {
        log.info("Received a response to the price snapshot:\n\t\t{}\n", serviceMessage.getData());
    }
}