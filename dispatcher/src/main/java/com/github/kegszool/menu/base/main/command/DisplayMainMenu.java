package com.github.kegszool.menu.base.main.command;

import com.github.kegszool.command.TextCommand;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayMainMenu extends TextCommand {

    private final static String START_COMMAND = "/start";

    private final String menuName;
    private final String command;
    private final String collapsibleCommandRu;
    private final String collapsibleCommandEn;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayMainMenu(
            @Value("${menu.main.name}") String menuName,
            @Value("${menu.main.command}") String command,
            @Value("${menu.main.command_for_collapsible.ru}") String collapsibleCommandRu,
            @Value("${menu.main.command_for_collapsible.en}") String collapsibleCommandEn,
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
        return this.command.equals(command) || START_COMMAND.equals(command) ||
                collapsibleCommandRu.equals(command) ||
                collapsibleCommandEn.equals(command);
    }

    @Override
    protected PartialBotApiMethod<?> handleCommand(Update update) {
        return messageUtils.applyMenuSequenceAndCreateMessage(update, menuName);
    }
}