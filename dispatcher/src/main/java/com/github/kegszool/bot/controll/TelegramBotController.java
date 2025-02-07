package com.github.kegszool.bot.controll;

import com.github.kegszool.bot.TelegramBot;
import com.github.kegszool.bot.service.BotRegistrationService;
import com.github.kegszool.bot.router.impl.ResponseRouter;
import com.github.kegszool.bot.router.impl.UpdateRouter;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class TelegramBotController {

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String botToken;
    
    private final BotRegistrationService botRegistrationService;
    private final UpdateRouter updateRouter;
    private final ResponseRouter responseRouter;

    private TelegramBot bot;

    @Autowired
    public TelegramBotController(
        BotRegistrationService botRegistrationService,
        UpdateRouter updateRouter,
        ResponseRouter responseRouter
    ) {
        this.botRegistrationService = botRegistrationService;
        this.updateRouter = updateRouter;
        this.responseRouter = responseRouter;
    }

    public void registerBot(TelegramBot bot) {
        this.bot = botRegistrationService.register(botToken, bot);
    }

    public void handleUpdate(Update update) {
        log.info("Received update:\n\t\t{}\n", update);
        try {
            PartialBotApiMethod<?> response = updateRouter.routeAndHandle(update, update);
            bot.sendAnswerMessage(response);
        } catch (Exception ex) { log.error("Error processing update:\n\t\t{}\n", update, ex); }
    }

    public void handleResponse(ServiceMessage<?> serviceMessage, String routingKey) {
        PartialBotApiMethod<?> response = responseRouter.routeAndHandle(serviceMessage, routingKey);
        if(response != null) {
            bot.sendAnswerMessage(response);
        }
    }
}