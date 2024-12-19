package com.github.kegszool.configuration;

import com.github.kegszool.TelegramBot;
import com.github.kegszool.exception.BotRegistrationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Log4j2
public class BotRegistrationService {

    public TelegramBot register(String botToken, TelegramBot bot) {
        try {
            var botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, bot);
            log.info("Successful registration of the bot");
        } catch (TelegramApiException e) {
            String errorMessage = "Bot registration error.";
            log.error(errorMessage, e);
            throw new BotRegistrationException(errorMessage, e);
        }
        return bot;
    }
}
