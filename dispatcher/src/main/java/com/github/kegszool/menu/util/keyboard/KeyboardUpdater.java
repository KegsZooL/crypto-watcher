package com.github.kegszool.menu.util.keyboard;

import java.util.List;
import java.util.ArrayList;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class KeyboardUpdater {

    public static InlineKeyboardMarkup removeButtonByCallbackData(InlineKeyboardMarkup markup, String callbackData) {
        List<InlineKeyboardRow> updatedRows = new ArrayList<>();

        for (InlineKeyboardRow row : markup.getKeyboard()) {
            List<InlineKeyboardButton> updatedButtons = row.stream()
                    .filter(button -> !callbackData.equals(button.getCallbackData()))
                    .toList();

            if (!updatedButtons.isEmpty()) {
                updatedRows.add(new InlineKeyboardRow(updatedButtons));
            }
        }

        markup.setKeyboard(updatedRows);
        return markup;
    }
}