package com.github.kegszool.notification.create;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.entity.service.UserService;
import com.github.kegszool.database.entity.mapper.impl.NotificationMapper;

import com.github.kegszool.database.repository.impl.CoinRepository;
import com.github.kegszool.database.repository.impl.NotificationRepository;

import com.github.kegszool.messaging.RequestExecutor;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;

import com.github.kegszool.user.UserDataBuilder;
import com.github.kegszool.user.UserNotFoundException;

@Log4j2
@Service
public class CreateNotificationExecutor implements RequestExecutor<NotificationDto, UserData> {

    private final String responseRoutingKey;
    private final CoinRepository coinRepository;

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final CreatedNotificationSender createdNotificationSender;

    private final UserService userService;
    private final UserDataBuilder userDataBuilder;

    @Autowired
    public CreateNotificationExecutor(
            @Value("${spring.rabbitmq.template.routing-key.create_notification.response}") String responseRoutingKey,
            NotificationRepository notificationRepository,
            UserService userService,
            NotificationMapper notificationMapper,
            CoinRepository coinRepository,
            CreatedNotificationSender createdNotificationSender,
            UserDataBuilder userDataBuilder
    ) {
        this.responseRoutingKey = responseRoutingKey;
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.notificationMapper = notificationMapper;
        this.coinRepository = coinRepository;
        this.createdNotificationSender = createdNotificationSender;
        this.userDataBuilder = userDataBuilder;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<NotificationDto> serviceMessage) {

        NotificationDto dto = serviceMessage.getData();
        Long telegramId = dto.getUser().getTelegramId();

        User user = userService.getUserByTelegramId(telegramId)
                .orElseThrow(() -> {
                    log.error("User with telegram ID {} not found", telegramId);
                    return new UserNotFoundException(telegramId);
                });

        String coinName = dto.getCoin().getName();
        Coin coin = coinRepository.findByName(coinName)
                .orElseGet(() -> coinRepository.save(new Coin(coinName)));

        Notification notification = notificationMapper.toEntity(dto, user, coin);
        notificationRepository.save(notification);

        UserData userData = userDataBuilder.buildUserData(user);

        createdNotificationSender.notify(coinName);
        return new ServiceMessage<>(serviceMessage.getMessageId(), serviceMessage.getChatId(), userData);
    }

    @Override
    public String getResponseRoutingKey() {
        return responseRoutingKey;
    }
}