package com.github.kegszool.controll;

import com.github.kegszool.TelegramBot;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@Log4j2
public class MessageRouter {

    private final InlineKeyboardHandler inlineKeyboardHandler;

    @Autowired
    public MessageRouter(InlineKeyboardHandler inlineKeyboardHandler) {
        this.inlineKeyboardHandler = inlineKeyboardHandler;
    }

    public void routeMessage(TelegramBot bot, Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            String chatId = message.getChatId().toString();

            if("/start".equals(text)) {
                handleStartCommand(bot, chatId);
            } else {
                notifyAboutUnsupportedCommand(bot, text, chatId);
            }
        }
        else if(update.hasCallbackQuery()) {
            handleCallbackQuery(bot, update.getCallbackQuery());
        }
    }

    private void handleStartCommand(TelegramBot bot, String chatId) {
        log.info("Handaling /start command for chat ID: {}", chatId);
        bot.sendAnswerMessage(createWelcomeMessage(chatId));
        var menuMessage = inlineKeyboardHandler.getAnswerMessageByCallbackDataType(chatId, "menu");
        bot.sendAnswerMessage(menuMessage);
    }

    private SendMessage createWelcomeMessage(String chatId) {
        String text = "Привет! Я бот, который поможет тебе уследить за изменениями курсов монет.\n\n" +
                "Для более удобного взаимодействия со мной можешь использовать меню";
        return new SendMessage(chatId, text);
    }

    private void notifyAboutUnsupportedCommand(TelegramBot bot, String command, String chatId) {
        log.warn("Unknow command received: {} from chatId: {}", command, chatId);
        String text = "Неподдерживаемая операция";
        SendMessage sendMessage = new SendMessage(chatId, text);
        bot.sendAnswerMessage(sendMessage);
    }

    private void handleCallbackQuery(TelegramBot bot, CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String callbackData = callbackQuery.getData();
        log.info("Handling callback query for chat ID: {}, callback data: {}", chatId, callbackData);

        var answerMessage = inlineKeyboardHandler.getAnswerMessageByCallbackDataType(chatId, callbackData);
        bot.sendAnswerMessage(answerMessage);
    }
}