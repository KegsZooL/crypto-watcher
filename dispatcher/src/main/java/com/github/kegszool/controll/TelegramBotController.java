package com.github.kegszool.controll;

import com.github.kegszool.service.ResponseConsumerService;
import com.github.kegszool.service.RequestProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotController {

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String botToken;

    private LongPollingUpdateConsumer bot;
    private final RequestProducerService updateProducer;
    private final ResponseConsumerService consumer;

    @Autowired
    public TelegramBotController(ResponseConsumerService consumer, RequestProducerService updateProducer) {
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

    public void sortMessageByType(Update update) {

    }
}