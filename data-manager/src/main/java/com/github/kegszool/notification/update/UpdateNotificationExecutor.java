package com.github.kegszool.notification.update;

import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.user.UserDataBuilder;
import com.github.kegszool.database.repository.impl.*;
import com.github.kegszool.database.entity.base.Notification;


import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.mapper.impl.*;

import com.github.kegszool.messaging.RequestExecutor;
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
    private final CoinMapper coinMapper;
    private final CoinRepository coinRepository;
    private final UpdatedNotificationSender updatedNotificationSender;
    private final UserDataBuilder userDataBuilder;
    private final NotificationMapper notificationMapper;

    @Autowired
    public UpdateNotificationExecutor(
            @Value("${spring.rabbitmq.template.routing-key.update_notification.response}") String routingKey,
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            CoinMapper coinMapper,
            CoinRepository coinRepository,
            UpdatedNotificationSender updatedNotificationSender,
            UserDataBuilder userDataBuilder,
            NotificationMapper notificationMapper
    ) {
        this.routingKey = routingKey;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.coinMapper = coinMapper;
        this.coinRepository = coinRepository;
        this.userDataBuilder = userDataBuilder;
        this.updatedNotificationSender = updatedNotificationSender;
        this.notificationMapper = notificationMapper;
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

                List<Notification> updatedNotifications = notificationRepository
                        .findByUser_IdAndCoin_IdAndInitialPriceAndTargetPercentageAndDirectionAndIsRecurring(
                                user.getId(),
                                coin.getId(),
                                dto.getInitialPrice(),
                                dto.getTargetPercentage(),
                                dto.getDirection(),
                                dto.isRecurring()
                        );

                if (!updatedNotifications.isEmpty()) {
                    for (Notification notification : updatedNotifications) {
                        notification.setLastTriggeredTime(dto.getLastTriggeredTime());
                        if (dto.isRecurring()) {
                            notification.setInitialPrice(dto.getTriggeredPrice());
                            notification.setTriggered(false);
                        	updated.add(notificationMapper.toDto(notification));
                        } else {
                            notification.setTriggered(true);
                        }
                        notificationRepository.save(notification);
                    }
                }
            }
            UserData userData = userDataBuilder.buildUserData(user);
            userDataSet.add(userData);
        }
        updatedNotificationSender.send(updated);
        return new ServiceMessage<>(STUB_MESSAGE_ID, STUB_CHAT_ID, new ArrayList<>(userDataSet));
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}