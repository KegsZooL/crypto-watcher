package com.github.kegszool.bot.router.impl;

import com.github.kegszool.bot.handler.UpdateHandler;
import com.github.kegszool.bot.router.AbstractRouter;
import com.github.kegszool.exception.bot.handler.HandlerNotFoundException;
import com.github.kegszool.exception.bot.handler.impl.UpdateHandlerNotFoundException;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Log4j2
public class UpdateRouter extends AbstractRouter<Update, UpdateHandler> {

    private final MessageUtils messageUtils;

    @Autowired
    public UpdateRouter(List<UpdateHandler> handlers, MessageUtils messageUtils) {
        super(handlers);
        this.messageUtils = messageUtils;
    }

    @Override
    protected boolean canHandle(UpdateHandler handler, Object key) {
        return handler.canHandle((Update) key);
    }

    @Override
    protected PartialBotApiMethod<?> handle(UpdateHandler handler, Update update) {
        return handler.handle(update);
    }

    @Override
    protected HandlerNotFoundException proccessMissingHandler(Update update, Object key) {
        String chatId = messageUtils.extractChatId(update);
        String warnMessage = "No handler was found for the update. ChatId: " + chatId;
        log.warn(warnMessage);
        return new UpdateHandlerNotFoundException(warnMessage);
    }
}