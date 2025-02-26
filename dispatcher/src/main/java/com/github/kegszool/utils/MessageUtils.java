package com.github.kegszool.utils;

import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.bot.menu.service.managment.MenuHistoryManager;
import com.github.kegszool.bot.menu.service.managment.MenuRegistry;

import com.github.kegszool.exception.bot.data.UnsupportedExtractFieldUpdateException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class MessageUtils {

    private final MenuHistoryManager menuHistoryManager;
    private final MenuRegistry menuRegistry;

    @Autowired
    public MessageUtils(
            MenuHistoryManager menuHistoryManager,
            MenuRegistry menuRegistry
    ) {
        this.menuRegistry = menuRegistry;
        this.menuHistoryManager = menuHistoryManager;
    }

    public EditMessageText createEditMessage(CallbackQuery query, String text) {
        return createEditMessage(query, text, null);
    }

    public EditMessageText createEditMessage(CallbackQuery query, String text, InlineKeyboardMarkup keyboard) {
        var message = query.getMessage();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        return EditMessageText.builder()
                .text(text)
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyboard)
                .parseMode(ParseMode.HTML)
                .build();
    }

    public EditMessageText createEditMessage(String chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
        return EditMessageText.builder()
                .text(text)
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyboard)
                .parseMode(ParseMode.HTML)
                .build();
    }

    public EditMessageText createEditMessageByMenuName(CallbackQuery query, String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        return createEditMessage(query, menu.getTitle(), menu.getKeyboardMarkup());
    }

    public EditMessageText createEditMessageByMenuName(CallbackQuery query, String title, String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        return createEditMessage(query, title, menu.getKeyboardMarkup());
    }

    public EditMessageText createEditMessageByMenuName(String chatId, Integer messageId, String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        return createEditMessage(chatId, messageId, menu.getTitle(), menu.getKeyboardMarkup());
    }

    public SendMessage createMessageByMenuName(String chatId, String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        return SendMessage.builder()
                .chatId(chatId)
                .text(menu.getTitle())
                .replyMarkup(menu.getKeyboardMarkup())
                .build();
    }

    public SendMessage recordAndCreateMessageByMenuName(String chatId, String menuName) {
        menuHistoryManager.recordMenu(chatId, menuName);
        return createMessageByMenuName(chatId, menuName);
    }

    public EditMessageText recordAndCreateEditMessageByMenuName(String chatId, Integer messageId, String menuName) {
        menuHistoryManager.recordMenu(chatId, menuName);
        return createEditMessageByMenuName(chatId, messageId, menuName);
    }

    public String extractChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        }
        throw new UnsupportedExtractFieldUpdateException("Cannot extract chatId from the given Update type");
    }

    public Integer extractMessageId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getMessageId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
        throw new UnsupportedExtractFieldUpdateException("Cannot extract messageId from the given Update type");
    }

    public String extractChatId(CallbackQuery query) {
       return query.getMessage().getChatId().toString();
    }
}