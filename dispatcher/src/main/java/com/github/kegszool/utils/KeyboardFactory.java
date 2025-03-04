package com.github.kegszool.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Log4j2
@Component
public class KeyboardFactory {

    @Value("${menu.action.back}")
    private String ACTION_BACK;

    @Value("${menu.action.prefix}")
    private String ACTION_PREFIX;

    private List<InlineKeyboardButton> createButtonsBySections(Map<String, String> sections) {
        return sections.entrySet().stream()
                .map(entry -> InlineKeyboardButton.builder()
                            .text(entry.getValue())
                            .callbackData(entry.getKey())
                            .build()
                ).collect(Collectors.toList());
    }

    public InlineKeyboardMarkup create(Map<String, String> sections, int buttonsPerRow, List<String> fullWidthSections) {

        List<InlineKeyboardRow> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = createButtonsBySections(sections);
        Set<String> fullWidthSectionsSet = new HashSet<>(fullWidthSections);

        AtomicReference<InlineKeyboardButton> backButtonRef = new AtomicReference<>();

        for (int i = 0; i < buttons.size(); i += buttonsPerRow) {
            int endIndex = Math.min(i + buttonsPerRow, buttons.size());
            List<InlineKeyboardButton> rowButtons = new ArrayList<>(buttons.subList(i, endIndex));

            rowButtons.removeIf(button -> {
                if (ACTION_BACK.equals(button.getCallbackData())) {
                    backButtonRef.set(button);
                    return true;
                }
                return false;
            });
            Map<Boolean, List<InlineKeyboardButton>> partitioned = rowButtons.stream()
                    .collect(Collectors.partitioningBy(button -> fullWidthSectionsSet.contains(button.getCallbackData())));

            if (!partitioned.get(false).isEmpty()) {
                rows.add(new InlineKeyboardRow(partitioned.get(false)));
            }
            partitioned.get(true).forEach(button -> rows.add(new InlineKeyboardRow(button)));
        }
        InlineKeyboardButton backButton = backButtonRef.get();
        if (backButton != null) {
            rows.add(new InlineKeyboardRow(backButton));
        }
        return new InlineKeyboardMarkup(rows);
    }
}