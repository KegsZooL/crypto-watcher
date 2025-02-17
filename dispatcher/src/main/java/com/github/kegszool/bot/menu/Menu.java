package com.github.kegszool.bot.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Menu {
    InlineKeyboardMarkup getKeyboard();
    String getTitle();
    String getName();
}