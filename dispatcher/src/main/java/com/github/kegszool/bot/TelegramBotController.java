package com.github.kegszool.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.ResponseRouter;

import com.github.kegszool.update.UpdateRouter;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.github.kegszool.menu.service.MenuCommandConfigurationService;

@Log4j2
@Component
public class TelegramBotController {

    private final UpdateRouter updateRouter;
    private final ResponseRouter responseRouter;
    private final BotRegistrationService botRegistrationService;
    private final MenuCommandConfigurationService menuCommandConfigurationService;

    private TelegramBot bot;

    @Autowired
    public TelegramBotController(
        UpdateRouter updateRouter,
        ResponseRouter responseRouter,
        BotRegistrationService botRegistrationService,
        MenuCommandConfigurationService menuCommandConfigurationService
    ) {
        this.updateRouter = updateRouter;
        this.responseRouter = responseRouter;
        this.botRegistrationService = botRegistrationService;
        this.menuCommandConfigurationService = menuCommandConfigurationService;
    }

    public void registerBot(TelegramBot bot) {
        this.bot = botRegistrationService.register(bot);
        menuCommandConfigurationService.init(bot);
    }

    public void handleUpdate(Update update) {
        log.info("Received update:\n\t\t{}\n", update);
        try {
            HandlerResult result = updateRouter.routeAndHandle(update, update);
            processResult(result);
        } catch (Exception ex) {
            log.error("Error processing update:\n\t\t{}\n", update, ex);
        }
    }

    public void handleResponse(ServiceMessage<?> serviceMessage, String routingKey) {
        try {
            HandlerResult result = responseRouter.routeAndHandle(serviceMessage, routingKey);
            processResult(result);
        } catch (Exception ex) {
            log.error("Error processing response", ex);
        }
    }

    private void processResult(HandlerResult result) {
        if (result instanceof HandlerResult.Success success) {
            bot.executeMsg(success.response());
        }
    }
}