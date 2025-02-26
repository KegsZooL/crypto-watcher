package com.github.kegszool.bot.menu.service.section;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class MenuSectionExtractor {

    public List<InlineKeyboardRow> extractAndTransformSections(
            InlineKeyboardMarkup sourceKeyboard,
            Predicate<? super InlineKeyboardButton> buttonFilter,
            String prefixButtonText,
            String prefixCallbackData
    ) {
        return sourceKeyboard.getKeyboard().stream()
                .map(row -> {
                    InlineKeyboardRow newRow = new InlineKeyboardRow();
                    row.stream()
                            .filter(buttonFilter)
                            .forEach(button -> {
                                InlineKeyboardButton transformedButton = new InlineKeyboardButton(
                                        prefixButtonText + button.getText()
                                );
                                transformedButton.setCallbackData(prefixCallbackData + button.getCallbackData());
                                newRow.add(transformedButton);
                            });
                    return newRow.isEmpty() ? null : newRow;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}