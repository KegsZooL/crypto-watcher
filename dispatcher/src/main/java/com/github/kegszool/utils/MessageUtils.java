package com.github.kegszool.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MessageUtils {

    public SendMessage createSendMessageByText(Update update, String text) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        var sendMessage = new SendMessage(chatId, text);
        return sendMessage;
    }

    public EditMessageText createEditMessageTextByCallbackQuery(CallbackQuery query, String title) {
        var message = query.getMessage();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        return EditMessageText.builder()
                .text(title)
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }

    public EditMessageText createEditMessageTextByCallbackQuery(CallbackQuery query, String title, InlineKeyboardMarkup inlineKeyboard) {
        var message = query.getMessage();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        return EditMessageText.builder()
                .text(title)
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(inlineKeyboard)
                .build();
    }
}