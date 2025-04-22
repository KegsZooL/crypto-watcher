package com.github.kegszool.coin.selection.state;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MenuSelectionBuffer {

    private final Map<String, List<InlineKeyboardButton>> menuNameToButtons = new ConcurrentHashMap<>();

    public void addSelected(String menuName, InlineKeyboardButton selectedButton) {
        menuNameToButtons.computeIfAbsent(menuName, k -> new ArrayList<>()).add(selectedButton);
    }

    public Optional<InlineKeyboardButton> removeSelected(String menuName, InlineKeyboardButton button) {
        return Optional.of(menuNameToButtons.get(menuName))
                .filter(list -> list.remove(button))
                .map(removed -> button);
    }

    public List<InlineKeyboardButton> getSelected(String menuName) {
        return menuNameToButtons.getOrDefault(menuName, Collections.emptyList());
    }

    public List<InlineKeyboardButton> removeSelected(String menuName) {
        return menuNameToButtons.remove(menuName);
    }
}