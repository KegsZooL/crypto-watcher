package com.github.kegszool.coin.selection.util.state.updater;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface SelectionDataUpdater {
    void update(InlineKeyboardButton button, String currentCallbackData);
}