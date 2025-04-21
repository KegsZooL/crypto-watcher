package com.github.kegszool.menu.service;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.coin.selection.state.updater.SelectionDataUpdater;
import com.github.kegszool.coin.selection.state.SelectionStateBuffer;
import com.github.kegszool.coin.selection.exception.SelectionButtonNotFoundException;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Log4j2
@Service
public abstract class MenuSelectionHandler {

    private final SelectionStateBuffer selectionStateBuffer;
    private final SelectionDataUpdater dataUpdater;
    private final MenuRegistry menuRegistry;
    private final MessageUtils messageUtils;

    protected MenuSelectionHandler(
            SelectionStateBuffer selectionStateBuffer,
            SelectionDataUpdater dataUpdater,
            MenuRegistry menuRegistry,
            MessageUtils messageUtils
    ) {
        this.selectionStateBuffer = selectionStateBuffer;
        this.dataUpdater = dataUpdater;
        this.menuRegistry = menuRegistry;
        this.messageUtils = messageUtils;
    }

    public PartialBotApiMethod<?> handleSelection(CallbackQuery callbackQuery, String menuName) {
        InlineKeyboardButton selectedButton = extractSelectedButton(callbackQuery, menuName);
        boolean isSelected = selectionStateBuffer.getSelected(menuName).contains(selectedButton);
        if (isSelected) {
            selectionStateBuffer.removeSelected(menuName, selectedButton);
        } else {
            selectionStateBuffer.addSelected(menuName, selectedButton);
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
                .orElseThrow(() -> createSelectionButtonNotFoundException(callback.getData(), menuName));
    }

    private SelectionButtonNotFoundException createSelectionButtonNotFoundException(
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