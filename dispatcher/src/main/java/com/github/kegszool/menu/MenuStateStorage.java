package com.github.kegszool.menu;

import java.util.Map;
import java.util.LinkedHashMap;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MenuStateStorage {

    private final Map<String, Map<String, LinkedHashMap<String, String>>> chatToMenuSections = new ConcurrentHashMap<>();
    private final Map<String, Map<String, InlineKeyboardMarkup>> chatToMenuKeyboards = new ConcurrentHashMap<>();
    private final Map<String, Map<String, String>> chatToMenuTitle = new ConcurrentHashMap<>();

    public void saveTitle(String menuName, String chatId, String title) {
        chatToMenuTitle
                .computeIfAbsent(menuName, k -> new ConcurrentHashMap<>())
                .put(chatId, title);
    }

    public String getTitle(String menuName, String chatId) {
        return chatToMenuTitle.getOrDefault(menuName, Map.of()).getOrDefault(chatId, "");
    }

    public void saveSections(String menuName, String chatId, LinkedHashMap<String, String> sections) {
        chatToMenuSections
                .computeIfAbsent(menuName, k -> new ConcurrentHashMap<>())
                .put(chatId, sections);
    }

    public void saveKeyboard(String menuName, String chatId, InlineKeyboardMarkup keyboard) {
        chatToMenuKeyboards
                .computeIfAbsent(menuName, k -> new ConcurrentHashMap<>())
                .put(chatId, keyboard);
    }

    public LinkedHashMap<String, String> getSections(String menuName, String chatId) {
        return chatToMenuSections.getOrDefault(menuName, Map.of()).get(chatId);
    }

    public InlineKeyboardMarkup getKeyboard(String menuName, String chatId) {
        return chatToMenuKeyboards.getOrDefault(menuName, Map.of()).get(chatId);
    }
}