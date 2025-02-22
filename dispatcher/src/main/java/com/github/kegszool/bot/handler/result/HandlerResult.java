package com.github.kegszool.bot.handler.result;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

public sealed interface HandlerResult {
    record Success(PartialBotApiMethod<?> response) implements HandlerResult {}
    record NoResponse() implements HandlerResult {}
}