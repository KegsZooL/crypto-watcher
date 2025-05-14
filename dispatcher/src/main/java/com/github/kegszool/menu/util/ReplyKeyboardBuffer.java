package com.github.kegszool.menu.util;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class ReplyKeyboardBuffer {

    private final Map<String, ReplyKeyboardMarkup> localeToKeyboard = new HashMap<>();

    public ReplyKeyboardBuffer(
            @Value("${menu.collapsible.first_row_titles.ru}") List<String> firstRowTitlesRu,
            @Value("${menu.collapsible.second_row_titles.ru}") String secondRowTitlesRu,
            @Value("${menu.collapsible.first_row_titles.en}") List<String> firstRowTitlesEn,
            @Value("${menu.collapsible.second_row_titles.en}") String secondRowTitlesEn
    ) {
        localeToKeyboard.put("ru", createKeyboard(firstRowTitlesRu, secondRowTitlesRu));
        localeToKeyboard.put("en", createKeyboard(firstRowTitlesEn, secondRowTitlesEn));
    }

    public ReplyKeyboardMarkup getByLocale(String locale) {
        return localeToKeyboard.getOrDefault(locale, localeToKeyboard.get("ru"));
    }

    private ReplyKeyboardMarkup createKeyboard(List<String> firstRowTitles, String secondRowTitle) {
        KeyboardRow firstRow = new KeyboardRow();
        firstRowTitles.forEach(title -> firstRow.add(new KeyboardButton(title)));

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton(secondRowTitle));

        return ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .selective(true)
                .keyboard(List.of(firstRow, secondRow))
                .build();
    }
}