package com.github.kegszool.bot.router.impl;

import com.github.kegszool.bot.handler.UpdateHandler;
import com.github.kegszool.bot.handler.UpsertUserHandler;
import com.github.kegszool.bot.handler.result.HandlerResult;
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
public class UpdateRouter extends AbstractRouter<Update, UpdateHandler, Update> {

    private final MessageUtils messageUtils;
    private final UpsertUserHandler upsertUserHandler;

    @Autowired
    public UpdateRouter(
            List<UpdateHandler> handlers,
            MessageUtils messageUtils,
            UpsertUserHandler upsertUserHandler
    ) {
        super(handlers);
        this.messageUtils = messageUtils;
        this.upsertUserHandler = upsertUserHandler;
    }
    
    @Override
    protected boolean canHandle(UpdateHandler handler, Update key) {
        return handler.canHandle(key);
    }

    @Override
    protected HandlerResult handle(UpdateHandler handler, Update update) {
        upsertUserHandler.handle(update);
        PartialBotApiMethod<?> response = handler.handle(update);
        return new HandlerResult.Success(response);
    }

    @Override
    protected HandlerNotFoundException proccessMissingHandler(Update update, Update key) {
        String chatId = messageUtils.extractChatId(update);
        String warnMessage = "No handler was found for the update. ChatId: " + chatId;
        log.warn(warnMessage);
        return new UpdateHandlerNotFoundException(warnMessage);
    }
}