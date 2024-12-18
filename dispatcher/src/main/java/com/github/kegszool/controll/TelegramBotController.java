package com.github.kegszool.controll;

import com.github.kegszool.service.ConsumerService;
import com.github.kegszool.service.UpdateProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotController {

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String botToken;

    private LongPollingUpdateConsumer bot;
    private final UpdateProducerService updateProducer;
    private final ConsumerService consumer;

    @Autowired
    public TelegramBotController(ConsumerService consumer, UpdateProducerService updateProducer) {
        this.consumer = consumer;
        this.updateProducer = updateProducer;
    }

    public void registerBot(LongPollingUpdateConsumer bot) {
        try {
            this.bot = bot;
            var botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}