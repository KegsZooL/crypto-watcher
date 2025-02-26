package com.github.kegszool.bot.menu.service.selection.controller;

import com.github.kegszool.bot.menu.service.managment.MenuRegistry;
import com.github.kegszool.bot.menu.service.selection.SelectionStateRepository;
import com.github.kegszool.bot.menu.service.selection.data_updater.SelectionDataUpdater;

import com.github.kegszool.utils.MessageUtils;
import com.github.kegszool.exception.bot.menu.selection.SelectionButtonNotFoundException;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Log4j2
@Service
public abstract class BaseSelectionController {

    private final SelectionStateRepository selectionStateRepository;
    private final SelectionDataUpdater dataUpdater;
    private final MenuRegistry menuRegistry;
    private final MessageUtils messageUtils;

    protected BaseSelectionController(
            SelectionStateRepository selectionStateRepository,
            SelectionDataUpdater dataUpdater,
            MenuRegistry menuRegistry,
            MessageUtils messageUtils
    ) {
        this.selectionStateRepository = selectionStateRepository;
        this.dataUpdater = dataUpdater;
        this.menuRegistry = menuRegistry;
        this.messageUtils = messageUtils;
    }

    public PartialBotApiMethod<?> handleSelection(CallbackQuery callbackQuery, String menuName) {
        InlineKeyboardButton selectedButton = extractSelectedButton(callbackQuery, menuName);
        boolean isSelected = selectionStateRepository.getSelected(menuName).contains(selectedButton);
        if (isSelected) {
            selectionStateRepository.removeSelected(menuName, selectedButton);
        } else {
            selectionStateRepository.addSelected(menuName, selectedButton);
        }
        dataUpdater.update(selectedButton, selectedButton.getCallbackData());
        return messageUtils.createEditMessageByMenuName(callbackQuery, menuName);
    }

    private InlineKeyboardButton extractSelectedButton(CallbackQuery callback, String menuName) {
        return menuRegistry.getMenu(menuName)
                .getKeyboardMarkup()
                .getKeyboard()
                .stream()
                .flatMap(Collection::stream)
                .filter(button -> callback.getData().equals(button.getCallbackData()))
                .findFirst()
                .orElseThrow(() -> handleSelectionButtonNotFoundException(callback.getData(), menuName));
    }

    private SelectionButtonNotFoundException handleSelectionButtonNotFoundException(
            String callbackData, String menuName
    ) {
        String errorMessage = String.format(
                "Button with callback data '%s' not found in menu '%s'.",
                callbackData, menuName
        );
        log.error(errorMessage);
        return new SelectionButtonNotFoundException(errorMessage);
    }
}