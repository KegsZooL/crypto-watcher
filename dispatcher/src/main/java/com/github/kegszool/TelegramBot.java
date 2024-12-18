package com.github.kegszool;

import com.github.kegszool.controll.TelegramBotController;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;

@Component
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;
    private final TelegramBotController controller;

    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken, TelegramBotController controller){
        this.client = new OkHttpTelegramClient(botToken);
        this.controller = controller;
    }

    @PostConstruct
    public void init() {
        controller.registerBot(this);
    }

    @Override
    public void consume(Update update) {
        controller.sortMessageByType(update);
    }

    public <T extends Serializable, Method extends PartialBotApiMethod<T>> void sendAnswerMessage(Method answerMessage) {
        try {
            client.execute((SendMessage)answerMessage);
        } catch (TelegramApiException ex) {

        }
    }
}