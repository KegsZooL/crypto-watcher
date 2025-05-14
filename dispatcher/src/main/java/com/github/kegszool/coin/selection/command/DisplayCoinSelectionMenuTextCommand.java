package com.github.kegszool.coin.selection.command;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.command.TextCommand;
import com.github.kegszool.messaging.util.MessageUtils;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayCoinSelectionMenuTextCommand extends TextCommand {

    private final String menuName;
    private final String command;
    private final String collapsibleCommandRu;
    private final String collapsibleCommandEn;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayCoinSelectionMenuTextCommand(
            @Value("${menu.coin_selection.name}") String menuName,
            @Value("${menu.coin_selection.command}") String command,
            @Value("${menu.coin_selection.command_for_collapsible.ru}") String collapsibleCommandRu,
            @Value("${menu.coin_selection.command_for_collapsible.en}") String collapsibleCommandEn,
            MessageUtils messageUtils
    ) {
        this.menuName = menuName;
        this.command = command;
        this.collapsibleCommandRu = collapsibleCommandRu;
        this.collapsibleCommandEn = collapsibleCommandEn;
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandleCommand(String command) {
        return this.command.equals(command) || collapsibleCommandRu.equals(command) ||
                collapsibleCommandEn.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        return messageUtils.applyMenuSequenceAndCreateMessage(update, menuName);
    }
}