package com.github.kegszool.UI.menu.impl;

import com.github.kegszool.UI.menu.Menu;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MainMenu implements Menu {

    private static String TITLE = "Основное меню";

    private InlineKeyboardMarkup selectionPanel;
    private List<InlineKeyboardButton> buttons;
    private List<InlineKeyboardRow> rows;

    private static Map<String, String> MENU_SECTIONS = Map.of(
            "exchange_rate", "Курс монет",
            "alerts", "Оповещения",
            "settings", "Настройки"
    );

    public MainMenu() {
        selectionPanel = createKeyboard();
    }

    private InlineKeyboardMarkup createKeyboard() {
        buttons = createButtons();
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(buttons);
        rows = Arrays.asList(keyboardRow);
        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardButton> createButtons() {
        List<String> сallbackDataSections = MENU_SECTIONS.keySet().stream().toList();
        return сallbackDataSections.stream()
                .map(callBackData -> {
                    String sectionName = MENU_SECTIONS.get(callBackData);
                    InlineKeyboardButton coinButton = new InlineKeyboardButton(sectionName);
                    coinButton.setCallbackData(callBackData);
                    return coinButton;
                }).collect(Collectors.toList());
    }

    public InlineKeyboardMarkup getMenu() {
        return selectionPanel;
    }

    public String getTitle (){
        return TITLE;
    }
}