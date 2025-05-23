package com.github.kegszool.update;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.router.AbstractRouter;
import com.github.kegszool.router.HandlerNotFoundException;
import com.github.kegszool.user.UserInitializationService;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.util.MessageUtils;

import com.github.kegszool.update.exception.UpdateHandlerNotFoundException;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

@Log4j2
@Component
public class UpdateRouter extends AbstractRouter<Update, UpdateHandler, Update> {

    private final MessageUtils messageUtils;
    private final UserInitializationService userInitializationService;

    @Autowired
    public UpdateRouter(
            List<UpdateHandler> handlers,
            MessageUtils messageUtils,
            UserInitializationService userInitializationService
    ) {
        super(handlers);
        this.messageUtils = messageUtils;
        this.userInitializationService = userInitializationService;
    }
    
    @Override
    protected boolean canHandle(UpdateHandler handler, Update key) {
        return handler.canHandle(key);
    }

    @Override
    protected HandlerResult handle(UpdateHandler handler, Update update) {
        userInitializationService.initializeIfFirstInteraction(update);
        PartialBotApiMethod<?> response = handler.handle(update);
        return new HandlerResult.Success(response);
    }

    @Override
    protected HandlerNotFoundException processMissingHandler(Update update, Update key) {
        String chatId = messageUtils.extractChatId(update);
        String warnMessage = "No handler was found for the update. ChatId: " + chatId;
        log.warn(warnMessage);
        return new UpdateHandlerNotFoundException(warnMessage);
    }
}