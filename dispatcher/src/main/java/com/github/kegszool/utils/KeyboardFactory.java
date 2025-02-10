package com.github.kegszool.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyboardFactory {

    private static String ACTION_BACK = "back";

    private static List<InlineKeyboardButton> createButtonsBySections(Map<String, String> sections) {
        List<String> callbackDataSections = sections.keySet().stream().toList();
        return callbackDataSections.stream()
                .map(callBackData -> {
                    String sectionName = sections.get(callBackData);
                    InlineKeyboardButton coinButton = new InlineKeyboardButton(sectionName);
                    coinButton.setCallbackData(callBackData);
                    return coinButton;
                }).collect(Collectors.toList());

    }

    public static InlineKeyboardMarkup create(Map<String, String> sections, int numberOfButtonsPerRow) {
        var buttons = createButtonsBySections(sections);

        List<InlineKeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i += numberOfButtonsPerRow) {

            List<InlineKeyboardButton> rowButtons = buttons.subList(i, Math.min(i + numberOfButtonsPerRow, buttons.size()));
            for (int j = 0; j < rowButtons.size(); j++) {
                InlineKeyboardButton currentButton = rowButtons.get(j);
                String currentCallbackData = currentButton.getCallbackData();
                if (ACTION_BACK.equals(currentCallbackData) && i + numberOfButtonsPerRow == buttons.size()) {
                    rowButtons = rowButtons.subList(0, rowButtons.size() - 1);
                    --i;
                }
            }
            rows.add(new InlineKeyboardRow(rowButtons));
        }
        return new InlineKeyboardMarkup(rows);
    }
}