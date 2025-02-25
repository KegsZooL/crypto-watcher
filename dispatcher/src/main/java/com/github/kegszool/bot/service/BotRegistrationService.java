package com.github.kegszool.bot.service;

import com.github.kegszool.bot.TelegramBot;
import com.github.kegszool.exception.bot.registration.BotRegistrationException;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BotRegistrationService {

    public TelegramBot register(String botToken, TelegramBot bot) {
        try {
            var botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, bot);
            log.info("Successful registration of the bot. Bot token: {}", botToken);
        } catch (TelegramApiException e) {
            var errorMessage = "Bot registration error. Bot token: " + botToken;
            log.error(errorMessage, e);
            throw new BotRegistrationException(errorMessage, e);
        }
        return bot;
    }
}
