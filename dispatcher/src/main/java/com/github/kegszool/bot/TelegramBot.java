package com.github.kegszool.bot;

import lombok.extern.log4j.Log4j2;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import com.github.kegszool.bot.exception.method.UnsupportedTelegramMethodTypeException;

@Log4j2
@Component
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;
    private final TelegramBotController controller;

    @Autowired
    public TelegramBot(
            @Value("${bot.token}") String botToken,
            TelegramBotController controller
    ) {
        this.client = new OkHttpTelegramClient(botToken);
        this.controller = controller;
    }
    @PostConstruct
    public void init() {
        controller.registerBot(this);
    }

    @Override
    public void consume(Update update) {
        controller.handleUpdate(update);
    }

    public void executeMsg(PartialBotApiMethod<?> msg) {
        try {
            switch (msg) {
                case SendMessage sendMessage -> client.execute(sendMessage);
                case EditMessageText editMessageText -> client.execute(editMessageText);
                case SetMyCommands commands -> client.execute(commands);
                default -> throw createUnsupportedMethodTypeException(msg);
            }
        } catch (TelegramApiException ex) {
            log.error("Error when sending a answer message: {}", msg, ex);
        }
    }

    private UnsupportedTelegramMethodTypeException createUnsupportedMethodTypeException(PartialBotApiMethod<?> msg) {
        String type = msg.getMethod();
        log.error("Unsupported method type: \"{}\"", type);
        return new UnsupportedTelegramMethodTypeException(type);
    }
}