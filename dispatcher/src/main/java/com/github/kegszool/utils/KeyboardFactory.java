package com.github.kegszool.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyboardFactory {

    //TODO: add docstrings for methods + add the func of stretching buttons to a certain width

    private static List<InlineKeyboardButton> createButtonsBySections(Map<String, String> sections) {
        List<String> сallbackDataSections = sections.keySet().stream().toList();
        return сallbackDataSections.stream()
                .map(callBackData -> {
                    String sectionName = sections.get(callBackData);
                    InlineKeyboardButton coinButton = new InlineKeyboardButton(sectionName);
                    coinButton.setCallbackData(callBackData);
                    return coinButton;
                }).collect(Collectors.toList());

    }

    public static InlineKeyboardMarkup create(Map<String, String> sections) {
        var buttons = createButtonsBySections(sections);
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(buttons);
        var rows = Arrays.asList(keyboardRow);
        return new InlineKeyboardMarkup(rows);
    }
}