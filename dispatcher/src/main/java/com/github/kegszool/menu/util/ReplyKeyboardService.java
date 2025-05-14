package com.github.kegszool.menu.util;

import org.springframework.stereotype.Component;
import com.github.kegszool.localization.LocalizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class ReplyKeyboardService {

    private final LocalizationService localizationService;
    private final ReplyKeyboardBuffer replyKeyboardBuffer;

    @Autowired
    public ReplyKeyboardService(LocalizationService localizationService, ReplyKeyboardBuffer replyKeyboardBuffer) {
        this.localizationService = localizationService;
        this.replyKeyboardBuffer = replyKeyboardBuffer;
    }

    private ReplyKeyboardMarkup getKeyboard(String chatId) {
        String currentLocale = localizationService.getLocale(chatId);
        return replyKeyboardBuffer.getByLocale(currentLocale);
    }

    public SendMessage attachKeyboard(SendMessage msg, String chatId) {
        msg.setReplyMarkup(getKeyboard(chatId));
        return msg;
    }

    public SendMessage attachKeyboard(SendMessage msg, String chatId, String locale) {
        msg.setReplyMarkup(replyKeyboardBuffer.getByLocale(locale));
        return msg;
    }
}