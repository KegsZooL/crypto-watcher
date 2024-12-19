package com.github.kegszool.controll;

import com.github.kegszool.UI.menu.impl.CoinExchangeRateMenu;
import com.github.kegszool.UI.menu.impl.MainMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class InlineKeyboardHandler {

    private final MainMenu mainMenu;
    private final CoinExchangeRateMenu coinExchangeRateMenu;

    @Autowired
    public InlineKeyboardHandler(
            MainMenu mainMenu,
            CoinExchangeRateMenu coinExchangeRateMenu
    ) {
        this.mainMenu = mainMenu;
        this.coinExchangeRateMenu = coinExchangeRateMenu;
    }

    public SendMessage getAnswerMessageByCallbackDataType(String chatId, String callbackData) {
        return switch (callbackData) {
            case "exchange_rate" -> new SendMessage(chatId, coinExchangeRateMenu.getTitle());
            case "menu" -> {
                var answerMessage = new SendMessage(chatId, mainMenu.getTitle());
                answerMessage.setReplyMarkup(mainMenu.getMenu());
                yield answerMessage;
            }
            default -> throw new IllegalStateException("Unexpected value: " + callbackData);
        };
    }
}