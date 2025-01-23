package com.github.kegszool.utils;

import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.bot.menu.service.MenuHistoryManager;
import com.github.kegszool.bot.menu.service.MenuRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MessageUtils {

    //TODO: переписать

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

    public EditMessageText createEditMessage(
            CallbackQuery query, String text, InlineKeyboardMarkup keyboard
    ) {
        var message = query.getMessage();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        return EditMessageText.builder()
                .text(text)
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyboard)
                .build();
    }

    public EditMessageText createEditMessageByMenuName(CallbackQuery query, String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        return createEditMessage(query, menu.getTitle(), menu.get());
    }

    public EditMessageText createEditMessageByMenuName(CallbackQuery query, String title,String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        return createEditMessage(query, title, menu.get());
    }

    public SendMessage createMessageByMenuName(String chatId, String menuName) {
        Menu mainMenu = menuRegistry.getMenu(menuName);
        var answerMessage = new SendMessage(chatId, mainMenu.getTitle());
        answerMessage.setReplyMarkup(mainMenu.get());
        return answerMessage;
    }

    public SendMessage recordAndCreateMessageMenu(String chatId, String menuName) {
        menuHistoryManager.recordMenu(chatId, menuName);
        return createMessageByMenuName(chatId, menuName);
    }

    public String extractChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        }
        return null;
    }

    public String extractChatId(CallbackQuery query) {
       return query.getMessage().getChatId().toString();
    }
}