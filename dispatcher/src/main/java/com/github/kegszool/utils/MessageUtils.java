package com.github.kegszool.utils;

import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.bot.menu.service.MenuRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MessageUtils {

    private final MenuRegistry menuRegistry;

    @Autowired
    public MessageUtils(MenuRegistry menuRegistry) {
        this.menuRegistry = menuRegistry;
    }

    public SendMessage createSendMessageByText(Update update, String text) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        var sendMessage = new SendMessage(chatId, text);
        return sendMessage;
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

    public EditMessageText createEditMessage(CallbackQuery query, String text) {
        return createEditMessage(query, text, null);
    }

    public EditMessageText createEditMessageByMenuName(CallbackQuery query, String menuName) {
        Menu menu = menuRegistry.getMenu(menuName);
        return createEditMessage(query, menu.getTitle(), menu.get());
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