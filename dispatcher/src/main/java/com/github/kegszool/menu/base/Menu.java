package com.github.kegszool.menu.base;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Menu {
    InlineKeyboardMarkup getKeyboardMarkup();
    String getTitle();
    String getName();
}