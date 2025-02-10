package com.github.kegszool.bot;

import com.github.kegszool.bot.controll.TelegramBotController;
import com.github.kegszool.exception.bot.data.method.execution.FailedExecutionMethodException;
import com.github.kegszool.exception.bot.data.method.execution.UnsupportedExecutionMethodException;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Log4j2
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;
    private final TelegramBotController controller;

    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken, TelegramBotController controller) {
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

    public void sendAnswerMessage(PartialBotApiMethod<?> answerMessage) {
        try {
            executeMethod(answerMessage);
        } catch (TelegramApiException ex) {
            handleFailedExecution(ex, answerMessage);
        }
    }

    private void executeMethod(PartialBotApiMethod<?> answerMessage) throws TelegramApiException {
        if (answerMessage instanceof EditMessageText editMessage) {
            client.execute(editMessage);
            log.info("EditMessage was executed:\n\n\"{}\"", editMessage);
        }
        else if (answerMessage instanceof SendMessage sendMessage) {
            client.execute((SendMessage)answerMessage);
            log.info("SendMessage was executed:\n\n\"{}\"", sendMessage);
        }
        else { handleExecutionUnsupportedMethod(answerMessage); }
    }

    private void handleExecutionUnsupportedMethod(PartialBotApiMethod<?> answerMessage) {
        String unsupportedMethod = answerMessage.getMethod();
        log.error("Unsupported method: \"{}\"", unsupportedMethod);
        throw new UnsupportedExecutionMethodException(unsupportedMethod);
    }

    private void handleFailedExecution(TelegramApiException ex, PartialBotApiMethod<?> answerMessage) {
        String method = answerMessage.getMethod();
        log.error("Execution error for method \"{}\"!", method, ex);
        throw new FailedExecutionMethodException("Failed execution for method: " + method, ex);
    }
}