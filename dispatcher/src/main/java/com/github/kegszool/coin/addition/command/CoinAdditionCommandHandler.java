package com.github.kegszool.coin.addition.command;

import java.util.List;
import org.springframework.stereotype.Component;
import com.github.kegszool.menu.ReplyKeyboardService;

import com.github.kegszool.coin.dto.UserCoinData;
import com.github.kegszool.user.messaging.dto.UserDto;
import com.github.kegszool.coin.addition.messaging.check_exists.CheckCoinExistsRequestProducer;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import com.github.kegszool.coin.addition.util.CoinCommandParser;
import com.github.kegszool.coin.addition.util.CoinAdditionMessageBuilder;

@Component
public class CoinAdditionCommandHandler {

    private final CoinCommandParser parser;
    private final CheckCoinExistsRequestProducer producer;
    private final CoinAdditionMessageBuilder messageBuilder;
    private final ReplyKeyboardService replyKeyboardService;

    public CoinAdditionCommandHandler(
            CoinCommandParser parser,
            CheckCoinExistsRequestProducer producer,
            CoinAdditionMessageBuilder messageBuilder,
            ReplyKeyboardService replyKeyboardService
    ) {
        this.parser = parser;
        this.producer = producer;
        this.messageBuilder = messageBuilder;
        this.replyKeyboardService = replyKeyboardService;
    }

    public SendMessage handle(Update update) {
        Message msg = update.getMessage();
        List<String> coins = parser.parse(msg.getText());

        if (coins.isEmpty()) {
            return messageBuilder.buildErrorMessage(msg);
        }
        sendRequest(msg, coins);
        SendMessage successMsg = messageBuilder.buildSuccessMessage(msg, coins);
        replyKeyboardService.attachKeyboard(successMsg, update.getMessage().getChatId().toString());
        return successMsg;
    }

    private void sendRequest(Message msg, List<String> coins) {
        User user = msg.getFrom();
        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName());
        UserCoinData userCoinData = new UserCoinData(userDto, coins);
        producer.send(userCoinData, msg);
    }
}