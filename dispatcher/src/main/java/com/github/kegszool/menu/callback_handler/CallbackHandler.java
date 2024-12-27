package com.github.kegszool.menu.callback_handler;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackHandler {
    boolean canHandle(String command);
    EditMessageText handle(CallbackQuery query);
}