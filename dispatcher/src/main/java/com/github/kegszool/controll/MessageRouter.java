package com.github.kegszool.controll;

import com.github.kegszool.menu.impl.MainMenu;
import com.github.kegszool.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@Log4j2
public class MessageRouter {

    private static final String START_COMMAND = "/start";
    private static final String UNSUPPORTED_COMMAND_MESSAGE = "Неподдерживаемое сообщение";

    private final MessageUtils messageUtils;
    private final InlineKeyboardHandler inlineKeyboardHandler;
    private final MainMenu mainMenu;

    @Autowired
    public MessageRouter(MessageUtils messageUtils, InlineKeyboardHandler inlineKeyboardHandler, MainMenu mainMenu) {
        this.messageUtils = messageUtils;
        this.inlineKeyboardHandler = inlineKeyboardHandler;
        this.mainMenu = mainMenu;
    }

    public SendMessage handleMessage(Update update) {
        Message message = update.getMessage();
        String text = message.getText();

        if (START_COMMAND.equals(text)) {
            return handleStartCommand(update);
        }
        else if(assertAddNewCoin()) {
            // TODO: inlineKeyboardHandler.addNewCoin();
        }
        return notifyAboutUnsupportedCommand(update);
    }

    private SendMessage handleStartCommand(Update update) {
        Long chatId = update.getMessage().getChatId();
        log.info("The '/start' command has been processed. ChatId: {}", chatId);
        String title = mainMenu.getTitle();
        var answerMessage = messageUtils.createSendMessageByText(update, title);
//        var mainMenu = inlineKeyboardHandler.getMainMenu();
//        answerMessage.setReplyMarkup(mainMenu);
        return answerMessage;
    }

    private boolean assertAddNewCoin() {
        return false;
    }

    private SendMessage notifyAboutUnsupportedCommand(Update update) {
        Message message = update.getMessage();
        String unsupportedCommand = message.getText();
        String chatId = message.getChatId().toString();

        log.warn("Unknown command received: {} from chatId: {}", unsupportedCommand, chatId);
        return messageUtils.createSendMessageByText(update, UNSUPPORTED_COMMAND_MESSAGE);
    }

}