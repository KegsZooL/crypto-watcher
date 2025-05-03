package com.github.kegszool.menu.base;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Menu {
    InlineKeyboardMarkup getKeyboardMarkup(String chatId);
    String getTitle();
    String getName();
}