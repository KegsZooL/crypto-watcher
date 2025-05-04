package com.github.kegszool.request.impl;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.base.UserPreference;
import com.github.kegszool.database.entity.mapper.impl.NotificationMapper;
import com.github.kegszool.database.entity.mapper.impl.UserPreferenceMapper;
import com.github.kegszool.database.entity.service.impl.UserService;
import com.github.kegszool.database.repository.impl.CoinRepository;
import com.github.kegszool.database.repository.impl.NotificationRepository;
import com.github.kegszool.database.repository.impl.UserRepository;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ResponseProducer;
import com.github.kegszool.notification.NotifierCreatedNotification;
import com.github.kegszool.request.RequestExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateNotificationExecutor implements RequestExecutor<NotificationDto, UserData> {

    private final String responseRoutingKey;
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final NotificationMapper notificationMapper;
    private final UserPreferenceMapper userPreferenceMapper;
    private final CoinRepository coinRepository;
    private final UserRepository userRepository;
    private final NotifierCreatedNotification notifierCreatedNotification;

    @Autowired
    public CreateNotificationExecutor(
            @Value("${spring.rabbitmq.template.routing-key.create_notification.response}") String responseRoutingKey,
            NotificationRepository notificationRepository,
            UserService userService,
            NotificationMapper notificationMapper,
            UserPreferenceMapper userPreferenceMapper,
            CoinRepository coinRepository,
            UserRepository userRepository,
            NotifierCreatedNotification notifierCreatedNotification
    ) {
        this.responseRoutingKey = responseRoutingKey;
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.notificationMapper = notificationMapper;
        this.userPreferenceMapper = userPreferenceMapper;
        this.coinRepository = coinRepository;
        this.userRepository = userRepository;
        this.notifierCreatedNotification = notifierCreatedNotification;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<NotificationDto> serviceMessage) {

        NotificationDto dto = serviceMessage.getData();
        Long telegramId = dto.getUser().getTelegramId();

        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new IllegalStateException("User must exist but was not found by Telegram ID: " + telegramId));

        String coinName = dto.getCoin().getName();
        Coin coin = coinRepository.findByName(coinName)
                .orElseGet(() -> coinRepository.save(new Coin(coinName)));

        Notification notification = notificationMapper.toEntity(dto, user, coin);
        notificationRepository.save(notification);

        UserData userData = new UserData();
        userData.setUser(dto.getUser());
        userData.setFavoriteCoins(userService.getUserFavoriteCoins(user.getId()));
        userData.setNotifications(userService.getUserNotifications(user.getId()));

        Optional<UserPreference> maybeUserPreference = userService.getUserPreference(user.getId());
        if (maybeUserPreference.isPresent()) {
            UserPreference userPreference = maybeUserPreference.get();
            userData.setUserPreference(userPreferenceMapper.toDto(userPreference));
        } else {
            //TODO: dummy
        }
        notifierCreatedNotification.notify(coinName);
        return new ServiceMessage<>(serviceMessage.getMessageId(), serviceMessage.getChatId(), userData);
    }

    @Override
    public String getResponseRoutingKey() {
        return responseRoutingKey;
    }
}