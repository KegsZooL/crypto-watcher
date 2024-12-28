package com.github.kegszool.controll;

import com.github.kegszool.menu.command.Command;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;
import java.util.Optional;

@Component
@Log4j2
public class MessageRouter {

    private static String UNSUPPORTED_COMMAND_MESSAGE = "Данное действие не может быть обработано!";

    private final List<Command> commands;
    private final MessageUtils messageUtils;

    @Autowired
    public MessageRouter(List<Command> commands, MessageUtils messageUtils) {
        this.commands = commands;
        this.messageUtils = messageUtils;
    }

    public PartialBotApiMethod<?> routeAndHandle(Update update) {
        String text = update.getMessage().getText();
        Optional<Command> command = commands.stream()
                .filter(cmd -> cmd.canHandle(text))
                .findFirst();

        if(command.isPresent()) {
            return command.get().execute(update);
        }
        return notifyAboutUnsupportedCommand(update);
    }

    private PartialBotApiMethod<?> notifyAboutUnsupportedCommand(Update update) {
        Message message = update.getMessage();
        String unsupportedCommand = message.getText();
        String chatId = message.getChatId().toString();

        log.warn("Unknown command received: {} from chatId: {}", unsupportedCommand, chatId);
        return messageUtils.createSendMessageByText(update, UNSUPPORTED_COMMAND_MESSAGE);
    }
}