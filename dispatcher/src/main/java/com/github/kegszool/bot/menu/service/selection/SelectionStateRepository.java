package com.github.kegszool.bot.menu.service.selection;

import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SelectionStateRepository {

    private final Map<String, List<InlineKeyboardButton>> menuNameSelectedButtonMapping = new ConcurrentHashMap<>();

    public void addSelected(String menuName, InlineKeyboardButton selectedButton) {
        menuNameSelectedButtonMapping.computeIfAbsent(menuName, k -> new ArrayList<>()).add(selectedButton);
    }

    public Optional<InlineKeyboardButton> removeSelected(String menuName, InlineKeyboardButton button) {
        return Optional.of(menuNameSelectedButtonMapping.get(menuName))
                .filter(list -> list.remove(button))
                .map(removed -> button);
    }

    public List<InlineKeyboardButton> getSelected(String menuName) {
        return menuNameSelectedButtonMapping.getOrDefault(menuName, Collections.emptyList());
    }

    public Optional<List<InlineKeyboardButton>> clearSelected(String menuName) {
        return Optional.ofNullable(menuNameSelectedButtonMapping.remove(menuName));
    }
}