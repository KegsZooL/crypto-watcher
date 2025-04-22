package com.github.kegszool.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.bot.exception.BotRegistrationException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Log4j2
@Service
public class BotRegistrationService {

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String BOT_TOKEN;

    public TelegramBot register(TelegramBot bot) {
        try {
            TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();
            application.registerBot(BOT_TOKEN, bot);
            log.info("Successful registration of the bot. Bot token: {}", BOT_TOKEN);
        } catch (TelegramApiException ex) { throw createRegistrationException(ex); }
        return bot;
    }

    private BotRegistrationException createRegistrationException(Exception ex) {
        log.error("Bot registration error. Bot token: ", BOT_TOKEN);
        return new BotRegistrationException("Bot token: " + BOT_TOKEN, ex);
    }
}