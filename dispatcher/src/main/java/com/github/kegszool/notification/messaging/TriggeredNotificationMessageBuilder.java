package com.github.kegszool.notification.messaging;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.localization.LocalizationService;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class TriggeredNotificationMessageBuilder {

    private final String menuName;
    private final String increasedMsgType;
    private final String decreasedMsgType;
    private final LocalizationService localizationService;

    @Autowired
    public TriggeredNotificationMessageBuilder(
            @Value("${menu.notification.name}") String menuName,
            @Value("${menu.notification.answer_messages.up.msg_type}") String increasedMsgType,
            @Value("${menu.notification.answer_messages.down.msg_type}") String decreasedMsgType,
            LocalizationService localizationService
    ) {
        this.menuName = menuName;
        this.localizationService = localizationService;
        this.increasedMsgType = increasedMsgType;
        this.decreasedMsgType = decreasedMsgType;
    }

    public SendMessage build(NotificationDto notification) {

        Long chatId = notification.getChatId();
        String localizedText = switch (notification.getDirection()) {
            case Up -> localizationService.getAnswerMessage(menuName, increasedMsgType, chatId.toString());
            case Down -> localizationService.getAnswerMessage(menuName, decreasedMsgType, chatId.toString());
        };

        localizedText = localizedText.replace("{coin}", notification.getCoin().getName())
                .replace("{change}", notification.getTargetPercentage().toString())
                .replace("{price}", Double.toString(notification.getInitialPrice()));

        return SendMessage.builder()
                .chatId(chatId)
                .text(localizedText)
                .parseMode(ParseMode.HTML)
                .build();
    }
}