package com.github.kegszool.notification.command;

import com.github.kegszool.command.TextCommand;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Component
public class DisplayNotificationMenuTextCommand extends TextCommand {

    private final String command;
    private final String menuName;
    private final String collapsibleCommandRu;
    private final String collapsibleCommandEn;
    private final MessageUtils messageUtils;

    @Autowired
    public DisplayNotificationMenuTextCommand(
            @Value("${menu.notification.command}") String command,
            @Value("${menu.notification.command_for_collapsible.ru}") String collapsibleCommandRu,
            @Value("${menu.notification.command_for_collapsible.en}") String collapsibleCommandEn,
            @Value("${menu.notification.name}") String menuName,
            MessageUtils messageUtils
    ) {
        this.command = command;
        this.collapsibleCommandRu = collapsibleCommandRu;
        this.collapsibleCommandEn = collapsibleCommandEn;
        this.menuName = menuName;
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