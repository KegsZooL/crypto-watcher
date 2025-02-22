package com.github.kegszool.bot.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Menu {
    InlineKeyboardMarkup getKeyboardMarkup();
    String getTitle();
    String getName();
}