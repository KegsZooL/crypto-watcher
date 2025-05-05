package com.github.kegszool.request.impl;

import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.repository.impl.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.mapper.impl.*;
import com.github.kegszool.database.entity.base.UserPreference;

import com.github.kegszool.request.RequestExecutor;
import com.github.kegszool.messaging.dto.database_entity.*;
import com.github.kegszool.messaging.dto.service.ServiceMessage;

@Log4j2
@Service
public class UpdateNotificationExecutor implements RequestExecutor<List<NotificationDto>, List<UserData>> {

    private final static Integer STUB_MESSAGE_ID = -1;
    private final static String STUB_CHAT_ID = "-1";

    private final String routingKey;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final CoinMapper coinMapper;
    private final UserMapper userMapper;
    private final FavoriteCoinRepository favoriteCoinRepository;
    private final FavoriteCoinMapper favoriteCoinMapper;
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserPreferenceMapper userPreferenceMapper;
    private final CoinRepository coinRepository;
    private final NotificationUpdatedSender notificationUpdatedSender;

    @Autowired
    public UpdateNotificationExecutor(
            @Value("${spring.rabbitmq.template.routing-key.update_notification.response}") String routingKey,
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            NotificationMapper notificationMapper,
            CoinMapper coinMapper,
            UserMapper userMapper,
            FavoriteCoinRepository favoriteCoinRepository,
            FavoriteCoinMapper favoriteCoinMapper,
            UserPreferenceRepository userPreferenceRepository,
            UserPreferenceMapper userPreferenceMapper,
            CoinRepository coinRepository,
            NotificationUpdatedSender notificationUpdatedSender
    ) {
        this.routingKey = routingKey;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.notificationMapper = notificationMapper;
        this.coinMapper = coinMapper;
        this.userMapper = userMapper;
        this.favoriteCoinRepository = favoriteCoinRepository;
        this.favoriteCoinMapper = favoriteCoinMapper;
        this.userPreferenceRepository = userPreferenceRepository;
        this.userPreferenceMapper = userPreferenceMapper;
        this.coinRepository = coinRepository;
        this.notificationUpdatedSender = notificationUpdatedSender;
    }

    @Override
    public ServiceMessage<List<UserData>> execute(ServiceMessage<List<NotificationDto>> serviceMessage) {

        List<NotificationDto> notifications = serviceMessage.getData();
        Map<Long, List<NotificationDto>> groupedByTelegramId = notifications.stream()
                .collect(Collectors.groupingBy(dto -> dto.getUser().getTelegramId()));

        Set<UserData> userDataSet = new HashSet<>();
        List<NotificationDto> updated = new ArrayList<>();

        for (Map.Entry<Long, List<NotificationDto>> entry : groupedByTelegramId.entrySet()) {

           	Long telegramId = entry.getKey();
            Optional<User> maybeUser = userRepository.findByTelegramId(telegramId);

            if (maybeUser.isEmpty()) {
                log.error("User was not found by telegram id '{}' contained in the notification", telegramId);
                continue;
            }

            User user = maybeUser.get();

            for (NotificationDto dto : entry.getValue()) {
                Coin coin = coinRepository.findByName(dto.getCoin().getName())
                        .orElseGet(() -> coinRepository.save(coinMapper.toEntity(dto.getCoin())));

                Optional<Notification> maybeNotification = notificationRepository
                        .findByUser_IdAndCoin_IdAndInitialPriceAndTargetPercentageAndDirectionAndIsRecurring(
                                user.getId(),
                                coin.getId(),
                                dto.getInitialPrice(),
                                dto.getTargetPercentage(),
                                dto.getDirection(),
                                dto.isRecurring()
                        );

                Notification notification = maybeNotification.orElseGet(() ->
                        notificationMapper.toEntity(dto, user, coin));

                notification.setTriggered(true);
                notificationRepository.save(notification);
            }

            int userId = user.getId();
            UserDto userDto = userMapper.toDto(user);
            List<FavoriteCoinDto> favoriteCoins = favoriteCoinRepository.findByUser_Id(userId).stream()
                    .map(favoriteCoinMapper::toDto)
                    .toList();

            List<NotificationDto> updatedNotifications = notificationRepository.findByUser_IdAndIsTriggeredFalse(userId).stream()
                    .map(notificationMapper::toDto)
                    .toList();

            updated.addAll(updatedNotifications);

            Optional<UserPreference> userPreference = userPreferenceRepository.findById(userId);
            UserPreferenceDto userPreferenceDto = userPreference
                    .map(userPreferenceMapper::toDto)
                    .orElse(new UserPreferenceDto(userDto, "ru"));

            UserData userData = new UserData(userDto, favoriteCoins, updatedNotifications, userPreferenceDto);
            userDataSet.add(userData);
        }
        notificationUpdatedSender.send(updated);
        return new ServiceMessage<>(STUB_MESSAGE_ID, STUB_CHAT_ID, new ArrayList<>(userDataSet));
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}