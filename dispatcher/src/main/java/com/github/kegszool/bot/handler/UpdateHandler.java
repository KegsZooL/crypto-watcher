package com.github.kegszool.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

public interface UpdateHandler {
    boolean canHandle(Update update);
    PartialBotApiMethod<?> handle(Update update);
}