package com.github.kegszool.notification.delete;

import java.util.Optional;

import com.github.kegszool.database.entity.mapper.impl.NotificationMapper;
import com.github.kegszool.notification.UnsubscriptionNotificationSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.entity.service.UserService;

import com.github.kegszool.database.repository.impl.CoinRepository;
import com.github.kegszool.database.repository.impl.NotificationRepository;

import com.github.kegszool.user.UserDataBuilder;
import com.github.kegszool.user.UserNotFoundException;

import com.github.kegszool.coin.CoinNotFoundException;
import com.github.kegszool.notification.NotificationIdentifierDto;

import com.github.kegszool.messaging.RequestExecutor;
import com.github.kegszool.messaging.dto.database_entity.*;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Log4j2
@Component
public class DeleteNotificationExecutor implements RequestExecutor<NotificationIdentifierDto, UserData> {

    private final String routingKey;
    private final UserService userService;
    private final NotificationMapper notificationMapper;
    private final UserDataBuilder userDataBuilder;
    private final CoinRepository coinRepository;
    private final NotificationRepository notificationRepository;
    private final UnsubscriptionNotificationSender unsubscriptionNotificationSender;

    @Autowired
    public DeleteNotificationExecutor(
            @Value("${spring.rabbitmq.template.routing-key.delete_notification.response}") String routingKey,
            UserService userService,
            CoinRepository coinRepository,
            NotificationRepository notificationRepository,
            NotificationMapper notificationMapper,
            UserDataBuilder userDataBuilder,
            UnsubscriptionNotificationSender unsubscriptionNotificationSender
    ) {
        this.routingKey = routingKey;
        this.userService = userService;
        this.coinRepository = coinRepository;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.userDataBuilder = userDataBuilder;
        this.unsubscriptionNotificationSender = unsubscriptionNotificationSender;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<NotificationIdentifierDto> serviceMessage) {
        NotificationIdentifierDto dto = serviceMessage.getData();
        Long telegramId = dto.getUserTelegramId();

        User user = userService.getUserByTelegramId(telegramId)
                .orElseThrow(() -> {
                    log.error("User with telegram ID {} not found", telegramId);
                    return new UserNotFoundException(telegramId);
                });

        String coinName = dto.getCoin().getName();
        Coin coin = coinRepository.findByName(coinName)
                .orElseThrow(() -> {
                    log.error("Coin with name {} not found", coinName);
                    return new CoinNotFoundException(coinName);
                });

        Optional<Notification> maybeNotification = notificationRepository.findByUser_IdAndMessageIdAndChatIdAndCoin_IdAndIsRecurringAndInitialPriceAndTargetPercentageAndDirection(
                user.getId(),
                dto.getMessageId(),
                dto.getChatId(),
                coin.getId(),
                dto.isRecurring(),
                dto.getInitialPrice(),
                dto.getTargetPercentage(),
                dto.getDirection()
        );

        maybeNotification.ifPresentOrElse(
                notification -> {
                    notification.setTriggered(true);
        			unsubscriptionNotificationSender.send(
                            notificationMapper.toDto(notification),
                            serviceMessage.getMessageId(),
                            serviceMessage.getChatId()
                    );
                    notificationRepository.save(notification);
                },
                () -> log.error("Notification not found for userId: {}, messageId: {}, chatId: {}, coinName: {}",
                        user.getId(), dto.getMessageId(), dto.getChatId(), coinName)
        );
        UserData userData = userDataBuilder.buildUserData(user);
        return new ServiceMessage<>(serviceMessage.getMessageId(), serviceMessage.getChatId(), userData);
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}