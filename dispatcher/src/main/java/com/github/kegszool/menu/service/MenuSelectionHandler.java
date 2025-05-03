package com.github.kegszool.menu.service;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import com.github.kegszool.menu.base.Menu;
import com.github.kegszool.messaging.util.MessageUtils;

import com.github.kegszool.coin.selection.util.state.MenuSelectionBuffer;
import com.github.kegszool.coin.selection.util.state.updater.SelectionDataUpdater;
import com.github.kegszool.coin.selection.exception.SelectionButtonNotFoundException;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


@Log4j2
@Service
public abstract class MenuSelectionHandler {

    private final MenuSelectionBuffer menuSelectionBuffer;
    private final SelectionDataUpdater dataUpdater;
    private final MenuRegistry menuRegistry;
    private final MessageUtils messageUtils;

    protected MenuSelectionHandler(
            MenuSelectionBuffer menuSelectionBuffer,
            SelectionDataUpdater dataUpdater,
            MenuRegistry menuRegistry,
            MessageUtils messageUtils
    ) {
        this.menuSelectionBuffer = menuSelectionBuffer;
        this.dataUpdater = dataUpdater;
        this.menuRegistry = menuRegistry;
        this.messageUtils = messageUtils;
    }

    public PartialBotApiMethod<?> handleSelection(CallbackQuery callbackQuery, String menuName) {
        InlineKeyboardButton selectedButton = extractSelectedButton(callbackQuery, menuName);
        boolean isSelected = menuSelectionBuffer.getSelected(menuName).contains(selectedButton);
        if (isSelected) {
            menuSelectionBuffer.removeSelected(menuName, selectedButton);
        } else {
            menuSelectionBuffer.addSelected(menuName, selectedButton);
        }
        dataUpdater.update(selectedButton, selectedButton.getCallbackData());
        return messageUtils.createEditMessageByMenuName(callbackQuery, menuName);
    }

    private InlineKeyboardButton extractSelectedButton(CallbackQuery callback, String menuName) {

        String chatId = messageUtils.extractChatId(callback);

        Menu menu = menuRegistry.getMenu(menuName, chatId);
        InlineKeyboardMarkup keyboardMarkup = menu.getKeyboardMarkup(chatId);
        List<InlineKeyboardRow> keyboard = keyboardMarkup.getKeyboard();

        if (keyboard.isEmpty()) {
            throw createSelectionButtonNotFoundException(callback.getData(), menuName);
        }

        for (List<InlineKeyboardButton> row : keyboard) {
            for (InlineKeyboardButton button : row) {
                if (callback.getData().equals(button.getCallbackData())) {
                    return button;
                }
            }
        }
        throw createSelectionButtonNotFoundException(callback.getData(), menuName);
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