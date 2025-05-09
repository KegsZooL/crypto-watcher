package com.github.kegszool.notification.creation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.messaging.util.MessageUtils;
import com.github.kegszool.messaging.dto.HandlerResult;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.response.BaseResponseHandler;

@Component
public class NotificationCreationStatusHandler extends BaseResponseHandler<Boolean> {

    private final String routingKey;
    private final String setNotificationMenuName;
    private final String answerMsgType;
    private final MessageUtils messageUtils;

    @Autowired
    public NotificationCreationStatusHandler(
            @Value("${spring.rabbitmq.template.routing-key.create_notification_response_from_exchange}") String routingKey,
            @Value("${menu.set_coin_notification.name}") String setNotificationMenuName,
            @Value("${menu.set_coin_notification.answer_messages.from_command.not_exists.msg_type}") String answerMsgType,
            MessageUtils messageUtils
    ) {
        this.routingKey = routingKey;
        this.setNotificationMenuName = setNotificationMenuName;
        this.answerMsgType = answerMsgType;
        this.messageUtils = messageUtils;
    }

    @Override
    public boolean canHandle(String routingKey) {
        return this.routingKey.equals(routingKey);
    }

    @Override
    public HandlerResult handle(ServiceMessage<Boolean> serviceMessage) {
        if (serviceMessage.getData()) {
            return new HandlerResult.NoResponse();
        }
        String chatId = serviceMessage.getChatId();
        return new HandlerResult.Success(messageUtils.createSendMessage(
                setNotificationMenuName, answerMsgType, chatId
        ));
    }
}