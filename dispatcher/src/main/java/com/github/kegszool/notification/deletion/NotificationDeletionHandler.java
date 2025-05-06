package com.github.kegszool.notification.deletion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.github.kegszool.menu.MenuStateStorage;
import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.notification.deletion.messaging.NotificationDeletionSender;
import com.github.kegszool.notification.deletion.messaging.NotificationIdentifierDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.coin.dto.CoinDto;
import com.github.kegszool.notification.messaging.dto.Direction;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class NotificationDeletionHandler {

    private final String menuName;
    private final NotificationDeletionSender sender;
    private final MenuStateStorage menuStateStorage;
    private final MessageUtils messageUtils;

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
        Integer callbackMessageId = msg.getMessageId();

        String payload = callbackData.substring(callbackPrefix.length());
        String[] parts = payload.split("_");

        BigDecimal targetPercentage = new BigDecimal(parts[0]);
        Direction direction = Direction.valueOf(parts[1]);
        boolean isRecurring = Boolean.parseBoolean(parts[2]);
        double initialPrice = Double.parseDouble(parts[3]);

        Integer notificationMsgId = Integer.parseInt(parts[4]);
        Long userTelegramId = Long.parseLong(parts[5]);
        String coinName = parts[6];

        CoinDto coinDto = new CoinDto();
        coinDto.setName(coinName);

        NotificationIdentifierDto dto = new NotificationIdentifierDto(
                userTelegramId,
                notificationMsgId,
                chatId,
                coinDto,
                isRecurring,
                initialPrice,
                targetPercentage,
                direction
        );
        sender.send(callbackMessageId, chatId.toString(), dto);

        InlineKeyboardMarkup keyboardMarkup = menuStateStorage.getKeyboard(menuName, chatId.toString());
        List<InlineKeyboardRow> rowsWithoutDeleted = new ArrayList<>();

        for (InlineKeyboardRow row : keyboardMarkup.getKeyboard()) {
            List<InlineKeyboardButton> updatedButtons = new ArrayList<>();

            for (InlineKeyboardButton button : row) {
                if (!callbackData.equals(button.getCallbackData())) {
                    updatedButtons.add(button);
                }
            }
            if (!updatedButtons.isEmpty()) {
                rowsWithoutDeleted.add(new InlineKeyboardRow(updatedButtons));
            }
        }
        keyboardMarkup.setKeyboard(rowsWithoutDeleted);
        menuStateStorage.saveKeyboard(menuName, chatId.toString(), keyboardMarkup);
        return messageUtils.createEditMessageByMenuName(callbackQuery, menuName);
    }
}