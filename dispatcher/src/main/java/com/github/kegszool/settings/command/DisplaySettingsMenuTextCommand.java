package com.github.kegszool.settings.command;

import com.github.kegszool.command.TextCommand;
import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.util.MessageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplaySettingsMenuTextCommand extends TextCommand {

    private final String menuName;
    private final String command;
    private final String collapsibleCommandRu;
    private final String collapsibleCommandEn;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplaySettingsMenuTextCommand(
            @Value("${menu.settings.name}") String menuName,
            @Value("${menu.settings.command}") String command,
            @Value("${menu.settings.command_for_collapsible.ru}") String collapsibleCommandRu,
            @Value("${menu.settings.command_for_collapsible.en}") String collapsibleCommandEn,
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