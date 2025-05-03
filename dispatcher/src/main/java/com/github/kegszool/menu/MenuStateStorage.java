package com.github.kegszool.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MenuStateStorage {

    private final Map<String, Map<String, LinkedHashMap<String, String>>> chatToMenuSections = new ConcurrentHashMap<>();
    private final Map<String, Map<String, InlineKeyboardMarkup>> chatToMenuKeyboards = new ConcurrentHashMap<>();

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

    public boolean hasMenuForChat(String menuName, String chatId) {
        return chatToMenuSections.getOrDefault(menuName, Map.of()).containsKey(chatId);
    }
}
