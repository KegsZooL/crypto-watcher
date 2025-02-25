package com.github.kegszool.bot.menu.service.section_transfer;

import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.bot.menu.service.MenuRegistry;
import com.github.kegszool.bot.menu.service.MenuSectionExtractor;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Set;
import java.util.List;
import java.util.Objects;
import java.util.Collection;
import java.util.function.Predicate;

import java.util.stream.Stream;
import java.util.stream.Collectors;

@Component
public abstract class BaseMenuSectionTransfer {

    private final MenuSectionExtractor sectionExtractor;
    private final MenuRegistry menuRegistry;

    protected String sourceMenuName, targetMenuName;
    protected Predicate<? super InlineKeyboardButton> buttonFilter;
    protected String prefixButtonText, prefixCallbackData;

    @Autowired
    public BaseMenuSectionTransfer(MenuSectionExtractor sectionExtractor, MenuRegistry menuRegistry) {
        this.sectionExtractor = sectionExtractor;
        this.menuRegistry = menuRegistry;
    }

    public void perform() {
        Menu sourceMenu = menuRegistry.getMenu(sourceMenuName);
        Menu targetMenu = menuRegistry.getMenu(targetMenuName);

        InlineKeyboardMarkup sourceKeyboardMarkup = sourceMenu.getKeyboardMarkup();
        InlineKeyboardMarkup targetKeyboardMarkup = targetMenu.getKeyboardMarkup();

        List<InlineKeyboardRow> transformedSections = sectionExtractor.extractAndTransformSections(
                sourceKeyboardMarkup,
                buttonFilter,
                prefixButtonText,
                prefixCallbackData
        );
        List<InlineKeyboardRow> mergedSections = mergeSectionsWithoutDuplicates(
               targetKeyboardMarkup.getKeyboard(),
               transformedSections
        );
        targetKeyboardMarkup.setKeyboard(mergedSections);
    }

    private List<InlineKeyboardRow> mergeSectionsWithoutDuplicates(
            List<InlineKeyboardRow> currentSections,
            List<InlineKeyboardRow> newSections
    ) {
        Set<String> existedCallbacks = currentSections.stream().
                flatMap(Collection::stream)
                .map(InlineKeyboardButton::getCallbackData)
                .collect(Collectors.toSet());

        List<InlineKeyboardRow> filteredNewSections = newSections.stream()
                .map(row -> {
                    InlineKeyboardRow filteredRow = new InlineKeyboardRow();
                    row.forEach(button -> {
                        if (!existedCallbacks.contains(button.getCallbackData())) {
                            filteredRow.add(button);
                        }
                    });
                    return filteredRow.isEmpty() ? null : filteredRow;
                })
                .filter(Objects::nonNull)
                .toList();
        return Stream.concat(filteredNewSections.stream(), currentSections.stream()).toList();
    }
}