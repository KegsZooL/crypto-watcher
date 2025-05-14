package com.github.kegszool.notification;

import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.notification.messaging.dto.Direction;
import com.github.kegszool.notification.messaging.dto.NotificationDto;
import com.github.kegszool.notification.creation.messaging.CreateNotificationRequestSender;

import com.github.kegszool.notification.util.NotificationBuilder;
import com.github.kegszool.notification.util.NotificationCommandParser;
import com.github.kegszool.notification.util.NotificationAnswerMessageBuilder;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
public class NotificationHandler {

    private final String currencySuffix;
    private final CreateNotificationRequestSender requestSender;
    private final NotificationContextBuffer contextBuffer;
    private final NotificationAnswerMessageBuilder messageBuilder;
    private final NotificationCommandParser notificationParser;

    @Autowired
    public NotificationHandler(
            @Value("${menu.coin_selection.prefix.currency}") String currencySuffix,
            CreateNotificationRequestSender requestSender,
            NotificationContextBuffer contextBuffer,
            NotificationAnswerMessageBuilder messageBuilder,
            NotificationCommandParser notificationParser
    ) {
        this.currencySuffix = currencySuffix;
        this.requestSender = requestSender;
        this.contextBuffer = contextBuffer;
        this.messageBuilder = messageBuilder;
        this.notificationParser = notificationParser;
    }

    public SendMessage createAndSendByFullCommand(Message msg) {

        String text = msg.getText();
        Long chatId = msg.getChatId();

        return notificationParser.parseIfValid(text)
                .map(cmd -> {

                    BigDecimal percentage = cmd.getPercentage();
                    boolean isRecurring = contextBuffer.getType(chatId).orElse(false);

                    if (!isValidPercentage(percentage, isRecurring)) {
                        return messageBuilder.createInvalidPercentageMsgFromMenu(chatId);
                    }

                    String coin = cmd.getCoin();
                    NotificationDto notification = NotificationBuilder.build(
                            msg.getFrom(),
                            msg.getMessageId(),
                            chatId,
                            coin + currencySuffix,
                            isRecurring,
                            percentage.abs(),
                            percentage.signum() > 0 ? Direction.Up : Direction.Down
                    );

                    sendAndClear(notification, msg.getMessageId(), chatId);
                    return messageBuilder.createSuccessMsgFromCommand(chatId, coin);
                })
                .orElse(messageBuilder.createErrorMsgFromCommand(chatId));
    }

    public SendMessage createAndSendByPercentageInput(Message msg) {

        Long chatId = msg.getChatId();
        Optional<String> selectedCoin = contextBuffer.getCoin(chatId);

        if (selectedCoin.isEmpty()) {
            return messageBuilder.createCoinNotSelectedMsgFromMenu(chatId);
        }

        try {
            String text = msg.getText().replace("%", "").replace("+", "").trim();

            BigDecimal percentage = new BigDecimal(text);
            boolean isRecurring = contextBuffer.getType(chatId).orElse(false);

            if (!isValidPercentage(percentage, isRecurring)) {
                return messageBuilder.createInvalidPercentageMsgFromMenu(chatId);
            }

            Direction direction = text.startsWith("-") ? Direction.Down : Direction.Up;

            String coin = selectedCoin.get();
            NotificationDto notification = NotificationBuilder.build(
                    msg.getFrom(),
                    msg.getMessageId(),
                    chatId,
                    coin + currencySuffix,
                    isRecurring,
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

    private boolean isValidPercentage(BigDecimal percentage, boolean isRecurring) {
        BigDecimal abs = percentage.abs();

        if (abs.precision() - abs.scale() > 4) {
            return false;
        }

        if (abs.scale() > 4) {
           return false;
        }

        if (isRecurring && abs.scale() > 1) {
            return false;
        }
        return true;
    }
}