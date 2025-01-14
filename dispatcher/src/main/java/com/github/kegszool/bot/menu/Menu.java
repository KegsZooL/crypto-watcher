package com.github.kegszool.bot.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Menu {
    InlineKeyboardMarkup get();
    String getTitle();
    String getPageName();
}
