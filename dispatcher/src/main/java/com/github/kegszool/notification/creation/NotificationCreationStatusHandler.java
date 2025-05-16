package com.github.kegszool.notification.creation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.github.kegszool.notification.creation.messaging.NotificationCreationStatus;

@Component
public class NotificationCreationStatusHandler extends BaseResponseHandler<NotificationCreationStatus> {

    private final String routingKey;
    private final String setNotificationMenuName;
    private final String unSuccessMsgType;
    private final String successMsgType;
    private final MessageUtils messageUtils;

    @Autowired
    public NotificationCreationStatusHandler(
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response_from_exchange}") String routingKey,
            @Value("${menu.set_coin_notification.name}") String setNotificationMenuName,
            @Value("${menu.set_coin_notification.answer_messages.from_command.not_exists.msg_type}") String unSuccessMsgTyp,
            @Value("${menu.set_coin_notification.answer_messages.from_menu.created.msg_type}") String successMsgType,
            MessageUtils messageUtils
    ) {
        this.routingKey = routingKey;
        this.setNotificationMenuName = setNotificationMenuName;
        this.successMsgType = successMsgType;
        this.unSuccessMsgType = unSuccessMsgTyp;
        this.messageUtils = messageUtils;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return this.routingKey.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<NotificationCreationStatus> serviceMessage) {

        String chatId = serviceMessage.getChatId();
        NotificationCreationStatus creationStatus = serviceMessage.getData();

        if (creationStatus.status()) {

            SendMessage sendMsg = messageUtils.createSendMessage(setNotificationMenuName, successMsgType, chatId);
            String prettyText = sendMsg.getText().replace("{coin}", creationStatus.coinName());
            sendMsg.setText(prettyText);

            return new HandlerResult.Success(sendMsg);
        }
        return new HandlerResult.Success(messageUtils.createSendMessage(
                setNotificationMenuName, unSuccessMsgType, chatId
        ));
    }
}