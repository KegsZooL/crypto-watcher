package com.github.kegszool.coin.selection.command;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.command.callback.CallbackCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayCoinSectionsEditor extends CallbackCommand {

    private final String command;
    private final String menuName;

    public DisplayCoinSectionsEditor(
            @Value("${menu.action.display_edit_coin_sections_menu}") String command,
            @Value("${menu.edit_coin_sections.name}") String menuName
    ) {
        this.command = command;
        this.menuName = menuName;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        EditMessageText answerMessage = messageUtils.recordAndCreateEditMessageByMenuName(
                callbackQuery, menuName
        );
        answerMessage.setParseMode(ParseMode.HTML);
        return answerMessage;
    }
}