package com.github.kegszool.notification;

import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.notification.messaging.dto.Direction;
import com.github.kegszool.notification.messaging.dto.NotificationDto;
import com.github.kegszool.notification.messaging.CreateNotificationRequestSender;

import com.github.kegszool.notification.util.NotificationBuilder;
import com.github.kegszool.notification.util.NotificationCommandParser;
import com.github.kegszool.notification.util.NotificationAnswerMessageBuilder;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;


@Component
public class NotificationHandler {

    private final CreateNotificationRequestSender requestSender;
    private final NotificationContextBuffer contextBuffer;
    private final NotificationAnswerMessageBuilder messageBuilder;
    private final NotificationBuilder notificationBuilder;
    private final NotificationCommandParser notificationParser;

    @Autowired
    public NotificationHandler(
            CreateNotificationRequestSender requestSender,
            NotificationContextBuffer contextBuffer,
            NotificationAnswerMessageBuilder messageBuilder,
            NotificationBuilder notificationBuilder,
            NotificationCommandParser notificationParser
    ) {
        this.requestSender = requestSender;
        this.contextBuffer = contextBuffer;
        this.messageBuilder = messageBuilder;
        this.notificationBuilder = notificationBuilder;
        this.notificationParser = notificationParser;
    }

    public SendMessage createByFullCommand(Message msg) {

        String text = msg.getText();
        Long chatId = msg.getChatId();

        return notificationParser.parseIfValid(text)
                .map(cmd -> {
                    BigDecimal percentage = cmd.getPercentage();
                    String coin = cmd.getCoin();
                    NotificationDto notification = notificationBuilder.build(
                            msg.getFrom(),
                            cmd.getCoin(),
                            true,
                            contextBuffer.getType(chatId),
                            percentage.abs(),
                            percentage.signum() > 0 ? Direction.Up : Direction.Down
                    );
                    sendAndClear(notification, msg.getMessageId(), chatId);
                    return messageBuilder.createSuccessMsgFromCommand(chatId, coin);
                })
                .orElse(messageBuilder.createErrorMsgFromCommand(chatId));
    }

    public SendMessage createByPercentageInput(Message msg) {

        Long chatId = msg.getChatId();
        Optional<String> selectedCoin = contextBuffer.getCoin(chatId);

        if (selectedCoin.isEmpty()) {
            return messageBuilder.createCoinNotSelectedMsgFromMenu(chatId);
        }

        try {
            String text = msg.getText().replace("%", "").replace("+", "").trim();
            BigDecimal percentage = new BigDecimal(text);
            Direction direction = text.startsWith("-") ? Direction.Down : Direction.Up;

            String coin = selectedCoin.get();

            NotificationDto notification = notificationBuilder.build(
                    msg.getFrom(),
                    coin,
                    true,
                    contextBuffer.getType(chatId),
                    percentage.abs(),
                    direction
            );
            sendAndClear(notification, msg.getMessageId(), chatId);
            return messageBuilder.createSuccessMsgFromMenu(chatId, coin);
        } catch (NumberFormatException ex) {
            return messageBuilder.createInvalidPercentageMsgFromMenu(chatId);
        }
    }

    private void sendAndClear(NotificationDto notification, Integer msgId, Long chatId) {
        requestSender.send(notification, msgId, chatId.toString());
        contextBuffer.clear(chatId);
    }

    public SendMessage delete(Update data) {
        return null;
    }
}