package com.github.kegszool.notification.deletion;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.MenuStateStorage;
import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.menu.util.keyboard.KeyboardUpdater;

import com.github.kegszool.notification.deletion.util.NotificationCallbackParser;
import com.github.kegszool.notification.deletion.messaging.NotificationIdentifierDto;
import com.github.kegszool.notification.deletion.messaging.NotificationDeletionSender;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class NotificationDeletionHandler {

    private final String menuName;
    private final MessageUtils messageUtils;
    private final NotificationDeletionSender sender;
    private final MenuStateStorage menuStateStorage;

    @Autowired
    public NotificationDeletionHandler(
            NotificationDeletionSender sender,
            MenuStateStorage menuStateStorage,
            @Value("${menu.notification_deletion.name}") String menuName,
            MessageUtils messageUtils
    ) {
        this.sender = sender;
        this.menuStateStorage = menuStateStorage;
        this.menuName = menuName;
        this.messageUtils = messageUtils;
    }

    public EditMessageText deleteAndCreateAnswerUpdateMsg(CallbackQuery callbackQuery, String callbackPrefix) {
        MaybeInaccessibleMessage msg = callbackQuery.getMessage();
        String callbackData = callbackQuery.getData();
        Long chatId = msg.getChatId();
        Integer messageId = msg.getMessageId();

        NotificationIdentifierDto dto = NotificationCallbackParser.parse(callbackData, callbackPrefix, chatId);
        sender.send(messageId, chatId.toString(), dto);

        InlineKeyboardMarkup keyboardMarkup = menuStateStorage.getKeyboard(menuName, chatId.toString());
        InlineKeyboardMarkup updatedMarkup = KeyboardUpdater.removeButtonByCallbackData(keyboardMarkup, callbackData);

        menuStateStorage.saveKeyboard(menuName, chatId.toString(), updatedMarkup);
        return messageUtils.createEditMessageByMenuName(callbackQuery, menuName);
    }
}