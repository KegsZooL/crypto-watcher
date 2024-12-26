package com.github.kegszool.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Menu {
    InlineKeyboardMarkup get();
    String getTitle();
    String getPageName();
}
