package com.github.kegszool.controll;

import com.github.kegszool.TelegramBot;
import com.github.kegszool.configuration.BotRegistrationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j2
public class TelegramBotController {

    @Value("${TELEGRAM_BOT_TOKEN}")
    private String botToken;

    private TelegramBot bot;
    private final BotRegistrationService botRegistrationService;
    private final MessageRouter messageRouter;
    private final CallbackQueryRouter callbackQueryRouter;

    private static final String ERROR_MESSAGE = "Произошла ошибка при выполнении программы";

    @Autowired
    public TelegramBotController(
        BotRegistrationService botRegistrationService,
        MessageRouter messageRouter,
        CallbackQueryRouter callbackQueryRouter
    ) {
        this.botRegistrationService = botRegistrationService;
        this.messageRouter = messageRouter;
        this.callbackQueryRouter = callbackQueryRouter;
    }

    public void registerBot(TelegramBot bot) {
        this.bot = botRegistrationService.register(botToken, bot);
    }

    public void hadleUpdate(Update update) {
        log.info("Received update {}", update);
        try {
            PartialBotApiMethod<?> response = null;
            if (update.hasMessage() && update.getMessage().hasText()) {
                response = messageRouter.routeAndHandle(update);
            } else if (update.hasCallbackQuery()) {
                response = callbackQueryRouter.routeAndHandle(update.getCallbackQuery());
            }
            bot.sendAnswerMessage(response);
        } catch (Exception e) { handleErrorUpdateProcessing(update); }
    }

    private void handleErrorUpdateProcessing(Update update) {
        log.error("Error processing update: {}", update);
        String chatId = extractChatId(update);
        if (chatId != null) {
            SendMessage errorMessage = new SendMessage(chatId, ERROR_MESSAGE);
            bot.sendAnswerMessage(errorMessage);
        } else {
            log.warn("Failed to determine chat ID for error message.");
        }
    }

    private String extractChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        }
        return null;
    }
}