package com.github.kegszool.controll;

import com.github.kegszool.TelegramBot;
import com.github.kegszool.configuration.BotRegistrationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j2
public class TelegramBotController {

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String botToken;

    private TelegramBot bot;
    private final BotRegistrationService botRegistrationService;
    private final MessageRouter messageRouter;

    @Autowired
    public TelegramBotController(
        BotRegistrationService botRegistrationService,
        MessageRouter messageRouter
    ) {
        this.botRegistrationService = botRegistrationService;
        this.messageRouter = messageRouter;
    }

    public void registerBot(TelegramBot bot) {
        this.bot = botRegistrationService.register(botToken, bot);
    }

    public void hadleUpdate(Update update) {
        log.info("Received update {}", update);
        try {
            messageRouter.routeMessage(bot, update);
        } catch (Exception e) {
            log.info("Failed to handle update", e);
        }
    }
}