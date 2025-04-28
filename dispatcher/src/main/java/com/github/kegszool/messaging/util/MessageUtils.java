package com.github.kegszool.messaging.util;

import com.github.kegszool.localization.LocalizationService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.base.Menu;
import com.github.kegszool.menu.service.MenuRegistry;
import com.github.kegszool.menu.service.MenuHistoryManager;
import com.github.kegszool.update.exception.UnsupportedExtractFieldUpdateException;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MessageUtils {

    private final MenuHistoryManager menuHistoryManager;
    private final MenuRegistry menuRegistry;
    private final LocalizationService localizationService;

    @Autowired
    public MessageUtils(
            MenuHistoryManager menuHistoryManager,
            MenuRegistry menuRegistry,
            LocalizationService localizationService
    ) {
        this.menuRegistry = menuRegistry;
        this.menuHistoryManager = menuHistoryManager;
        this.localizationService = localizationService;
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
        String localizedTitle = localizationService.getTitleText(menuName);
        return createEditMessage(query, localizedTitle, menu.getKeyboardMarkup());
    }

    public EditMessageText createEditMessageByMenuNameWithLocale(CallbackQuery query, String menuName, String locale) {
        Menu menu = menuRegistry.getMenu(menuName);
        String localizedTitle = localizationService.getTitleText(menuName, locale);
        return createEditMessage(query, localizedTitle, menu.getKeyboardMarkup());
    }

    public EditMessageText createEditMessageByMenuName(CallbackQuery query, String title, String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        return createEditMessage(query, title, menu.getKeyboardMarkup());
    }

    public EditMessageText createEditMessageByMenuName(String chatId, Integer messageId, String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        String localizedTitle = localizationService.getTitleText(menuName);
        return createEditMessage(chatId, messageId, localizedTitle, menu.getKeyboardMarkup());
    }

    public SendMessage createMessageByMenuName(String chatId, String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        String localizedTitle = localizationService.getTitleText(menuName);
        return SendMessage.builder()
                .chatId(chatId)
                .text(localizedTitle)
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