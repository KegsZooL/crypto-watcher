package com.github.kegszool.notification;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.notification.messaging.CreateNotificationRequestSender;
import com.github.kegszool.notification.messaging.dto.Direction;
import com.github.kegszool.notification.messaging.dto.NotificationDto;
import com.github.kegszool.notification.util.NotificationAnswerMessageBuilder;
import com.github.kegszool.notification.util.NotificationCommandParser;
import com.github.kegszool.user.messaging.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;

@Component
public class NotificationHandler {

    private final CreateNotificationRequestSender requestSender;
    private final NotificationContextBuffer contextBuffer;
    private final NotificationAnswerMessageBuilder messageBuilder;
    private final NotificationCommandParser notificationParser;

    @Autowired
    public NotificationHandler(
            CreateNotificationRequestSender requestSender,
            NotificationContextBuffer contextBuffer,
            NotificationAnswerMessageBuilder messageBuilder,
            NotificationCommandParser notificationParser
    ) {
        this.requestSender = requestSender;
        this.contextBuffer = contextBuffer;
        this.messageBuilder = messageBuilder;
        this.notificationParser = notificationParser;
    }

    public SendMessage createByFullCommand(Message message) {

        String text = message.getText();
        Long chatId = message.getChatId();

        Optional<ParsedNotificationCommand> maybeParsedNotification = notificationParser.parseIfValid(text);
        if (maybeParsedNotification.isPresent()) {
            ParsedNotificationCommand cmd = maybeParsedNotification.get();
            NotificationDto notification = new NotificationDto();

            User user = message.getFrom();

            notification.setCoin(new CoinDto(cmd.getCoin()));
            notification.setUser(new UserDto(user.getId(), user.getFirstName(), user.getLastName()));

            notification.setTargetPercentage(cmd.getPercentage());
            notification.setDirection(contextBuffer.getType(chatId).);
        }

        return messageBuilder.createErrorMessage();
    }


    public SendMessage createByPercentageInput() {
        return null;
    }

    public SendMessage delete(Update data) {
        return null;
    }
}