package com.github.kegszool.messaging.util;

import com.github.kegszool.menu.base.BaseMenu;
import com.github.kegszool.menu.service.CalledMenuManager;
import org.springframework.stereotype.Component;
import com.github.kegszool.localization.LocalizationService;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.MenuStateStorage;
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
    private final MenuStateStorage menuStateStorage;
    private final CalledMenuManager calledMenuManager;

    @Autowired
    public MessageUtils(
            MenuStateStorage menuStateStorage,
            MenuHistoryManager menuHistoryManager,
            MenuRegistry menuRegistry,
            LocalizationService localizationService,
            CalledMenuManager calledMenuManager
    ) {
        this.menuStateStorage = menuStateStorage;
        this.menuRegistry = menuRegistry;
        this.menuHistoryManager = menuHistoryManager;
        this.localizationService = localizationService;
        this.calledMenuManager = calledMenuManager;
    }

    public SendMessage createSendMessage(String menuName, String answerMsgType, String chatId) {
        String title = localizationService.getAnswerMessage(menuName, answerMsgType, chatId);
        return SendMessage.builder()
                .text(title)
                .chatId(chatId)
                .parseMode(ParseMode.HTML)
                .build();
    }

    public EditMessageText createEditMessage(CallbackQuery query, String text) {
        return createEditMessage(query, text, null);
    }

    public EditMessageText createEditMessage(CallbackQuery query, String text, InlineKeyboardMarkup keyboard) {
        return createEditMessage(
                query.getMessage().getChatId().toString(),
                query.getMessage().getMessageId(),
                text,
                keyboard
        );
    }

    public EditMessageText createEditMessage(String chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(keyboard)
                .parseMode(ParseMode.HTML)
                .build();
    }

    public SendMessage createMessageByMenuName(String chatId, String menuName) {
        var menuData = getMenuWithTitle(menuName, chatId);
        return SendMessage.builder()
                .chatId(chatId)
                .text(menuData.localizedTitle())
                .replyMarkup(menuData.menu().getKeyboardMarkup(chatId))
                .build();
    }

    public SendMessage applyMenuSequenceAndCreateMessage(Update update, String menuName) {
        String chatId = extractChatId(update);
        calledMenuManager.applySequence(chatId, menuName);
        return createMessageByMenuName(chatId, menuName);
    }

    public EditMessageText createEditMessageByMenuName(CallbackQuery query, String menuName) {
        String chatId = extractChatId(query);
        var menuData = getMenuWithTitle(menuName, extractChatId(query));
        return createEditMessage(query, menuData.localizedTitle(), menuData.menu().getKeyboardMarkup(chatId));
    }

    public EditMessageText createEditMessageByMenuNameWithLocale(CallbackQuery query, String menuName, String locale) {
        String chatId = extractChatId(query);
        var menu = menuRegistry.getMenu(menuName, chatId);
        var title = localizationService.getTitleTextByLocale(menuName, locale);
        return createEditMessage(query, title, menu.getKeyboardMarkup(chatId));
    }

    public EditMessageText createEditMessageByMenuName(CallbackQuery query, String title, String menuName) {
        String chatId = extractChatId(query);
        var menu = menuRegistry.getMenu(menuName, chatId);
        return createEditMessage(query, title, menu.getKeyboardMarkup(chatId));
    }

    public EditMessageText createEditMessageByMenuName(String chatId, Integer messageId, String menuName) {
        var menuData = getMenuWithTitle(menuName, chatId);
        return createEditMessage(chatId, messageId, menuData.localizedTitle(), menuData.menu().getKeyboardMarkup(chatId));
    }

    public EditMessageText recordAndCreateEditMessageByMenuName(CallbackQuery query, String menuName) {
        var msg = query.getMessage();
        return recordAndCreateEditMessageByMenuName(msg.getChatId().toString(), msg.getMessageId(), menuName);
    }

    public EditMessageText recordAndCreateEditMessageByMenuName(String chatId, Integer messageId, String menuName) {
        menuHistoryManager.recordMenu(chatId, menuName);
        return createEditMessageByMenuName(chatId, messageId, menuName);
    }

    public EditMessageText recordAndCreateEditMessageByMenuNameWitCurrentLocal(CallbackQuery query, String menuName) {
        String chatId = extractChatId(query);
        menuHistoryManager.recordMenu(extractChatId(query), menuName);
        return createEditMessageByMenuNameWithLocale(query, menuName, localizationService.getLocale(chatId));
    }

    public String extractChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            return extractChatId(update.getCallbackQuery());
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

    private record MenuData(BaseMenu menu, String localizedTitle) {}

    private MenuData getMenuWithTitle(String menuName, String chatId) {
        BaseMenu menu = menuRegistry.getMenu(menuName, chatId);

        String title;
        if (menu.hasTitleBuilder()) {
            title = menuStateStorage.getTitle(menuName, chatId);
        } else {
        	title = localizationService.getTitleText(menuName, chatId);
        }
        return new MenuData(menu, title);
    }
}