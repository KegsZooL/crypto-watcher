package com.github.kegszool.bot.menu.service.section.section_transfer;

import com.github.kegszool.bot.menu.Menu;
import com.github.kegszool.bot.menu.service.managment.MenuRegistry;
import com.github.kegszool.bot.menu.service.section.MenuSectionExtractor;
import com.github.kegszool.exception.bot.menu.configuration.section.transfer.IllegalTransferredCallbackDataException;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
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

@Log4j2
@Component
public abstract class BaseMenuSectionTransfer {

    private final MenuSectionExtractor sectionExtractor;
    private final MenuRegistry menuRegistry;

    protected String sourceMenuName, targetMenuName;
    protected Predicate<? super InlineKeyboardButton> buttonFilter;
    protected String prefixButtonText, prefixCallbackData;

    @Value("${menu.action.prefix}")
    private String ACTION_PREFIX;

    @Value("${menu.action.selected_prefix}")
    private String SELECTED_OBJECT_PREFIX;

    @Value("${menu.action.unselected_prefix}")
    private String UNSELECTED_OBJECT_PREFIX;

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
            List<InlineKeyboardRow> targetSections,
            List<InlineKeyboardRow> sourceSections
    ) {
        Set<String> targetIdsWithoutAction = targetSections.stream()
                .flatMap(Collection::stream)
                .filter(button -> isSelectableButton(button.getCallbackData()))
                .map(button -> extractBaseId(button.getCallbackData()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<InlineKeyboardRow> filteredSourceSections = sourceSections.stream()
                .map(row -> {
                    InlineKeyboardRow filteredRow = new InlineKeyboardRow();
                    row.forEach(button -> {
                        String callbackData = button.getCallbackData();
                        if (isActionButton(callbackData)) {
                            filteredRow.add(button);
                        } else if (isSelectableButton(callbackData)) {
                            String baseId = extractBaseId(callbackData);
                            if (baseId != null && !targetIdsWithoutAction.contains(baseId)) {
                                filteredRow.add(button);
                            }
                        }
                    });
                    return filteredRow.isEmpty() ? null : filteredRow;
                })
                .filter(Objects::nonNull)
                .toList();

        return Stream.concat(filteredSourceSections.stream(), targetSections.stream()).toList();
    }

    private boolean isSelectableButton(String callbackData) {
        return callbackData != null &&
                (callbackData.startsWith(UNSELECTED_OBJECT_PREFIX) || callbackData.startsWith(SELECTED_OBJECT_PREFIX));
    }

    private boolean isActionButton(String callbackData) {
        return callbackData != null && callbackData.startsWith(ACTION_PREFIX);
    }

    private String extractBaseId(String callbackData) {
        if (callbackData == null) {
            throw handleIllegalCallbackData(callbackData);
        }
        String[] parts = callbackData.split("_", 2);
        if (parts.length == 2) {
            return parts[1];
        } else {
            throw handleIllegalCallbackData(callbackData);
        }
    }

    private IllegalTransferredCallbackDataException handleIllegalCallbackData(String callbackData) {
        log.error("Error during extraction of the base id for callback data \"{}\" in transferred object", callbackData);
        return new IllegalTransferredCallbackDataException("Callback data: " + callbackData);
    }
}