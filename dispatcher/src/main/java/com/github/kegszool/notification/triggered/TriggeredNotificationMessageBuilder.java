package com.github.kegszool.notification.triggered;

import org.springframework.stereotype.Component;
import com.github.kegszool.menu.ReplyKeyboardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.github.kegszool.localization.LocalizationService;
import com.github.kegszool.notification.messaging.dto.NotificationDto;

@Component
public class TriggeredNotificationMessageBuilder {

    private final String menuName;
    private final String increasedMsgType;
    private final String decreasedMsgType;
    private final LocalizationService localizationService;
    private final ReplyKeyboardService replyKeyboardService;

    @Autowired
    public TriggeredNotificationMessageBuilder(
            @Value("${menu.notification.name}") String menuName,
            @Value("${menu.notification.answer_messages.up.msg_type}") String increasedMsgType,
            @Value("${menu.notification.answer_messages.down.msg_type}") String decreasedMsgType,
            LocalizationService localizationService,
            ReplyKeyboardService replyKeyboardService
    ) {
        this.menuName = menuName;
        this.localizationService = localizationService;
        this.increasedMsgType = increasedMsgType;
        this.decreasedMsgType = decreasedMsgType;
        this.replyKeyboardService = replyKeyboardService;
    }

    public SendMessage build(NotificationDto notification) {

        Long chatId = notification.getChatId();
        String localizedText = switch (notification.getDirection()) {
            case Up -> localizationService.getAnswerMessage(menuName, increasedMsgType, chatId.toString());
            case Down -> localizationService.getAnswerMessage(menuName, decreasedMsgType, chatId.toString());
        };

        double initialPrice = notification.getInitialPrice();
        double triggeredPrice = notification.getTriggeredPrice();

        double actualChange = ((triggeredPrice - initialPrice) / initialPrice) * 100.0;

        String changeFormatted;
        if (notification.isRecurring()) {
            changeFormatted = String.format("%.2f", actualChange);
        } else {
            changeFormatted = String.format("%.3f", actualChange);
        }
        localizedText = localizedText.replace("{coin}", notification.getCoin().getName())
                .replace("{change}", changeFormatted)
                .replace("{price}", Double.toString(triggeredPrice));


        SendMessage answerMsg = SendMessage.builder()
                .chatId(chatId)
                .text(localizedText)
                .parseMode(ParseMode.HTML)
                .build();

        replyKeyboardService.attachKeyboard(answerMsg, chatId.toString());
        return answerMsg;
    }
}