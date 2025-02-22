package com.github.kegszool.bot.menu.command.callback.impl;

import com.github.kegszool.bot.menu.command.callback.CallbackCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class EditCoinSectionsMenuCommand extends CallbackCommand {

    @Value("${menu.action.open_edit_coin_sections_menu}")
    private String MENU_ACTION_EDIT_COIN_SECTIONS;

    @Value("${menu.edit_coin_sections.name}")
    private String EDIT_COIN_SECTIONS_MENU_NAME;

    @Override
    protected boolean canHandleCommand(String command) {
        return MENU_ACTION_EDIT_COIN_SECTIONS.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(CallbackQuery callbackQuery) {
        String chatId = messageUtils.extractChatId(callbackQuery);
        Integer messageId = callbackQuery.getMessage().getMessageId();

        var answerMessage = messageUtils.recordAndCreateEditMessageByMenuName(
                chatId, messageId, EDIT_COIN_SECTIONS_MENU_NAME
        );
        answerMessage.setParseMode(ParseMode.HTML);
        return answerMessage;
    }
}