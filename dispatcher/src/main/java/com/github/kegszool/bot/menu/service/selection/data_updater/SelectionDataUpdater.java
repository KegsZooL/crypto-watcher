package com.github.kegszool.bot.menu.service.selection.data_updater;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface SelectionDataUpdater {
    void update(InlineKeyboardButton button, String currentCallbackData);
}