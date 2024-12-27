package com.github.kegszool.menu.command;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    boolean canHandle(String command);
    PartialBotApiMethod<?> execute(Update update);
}
