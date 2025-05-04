package com.github.kegszool.database.entity.mapper.impl;

import com.github.kegszool.database.entity.base.Coin;
import com.github.kegszool.database.entity.base.Notification;
import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.mapper.EntityMapper;

import org.springframework.stereotype.Component;
import com.github.kegszool.messaging.dto.database_entity.NotificationDto;

@Component
public class NotificationMapper extends EntityMapper<Notification, NotificationDto> {

    private final UserMapper userMapper;
    private final CoinMapper coinMapper;

    public NotificationMapper(UserMapper userMapper, CoinMapper coinMapper) {
        this.userMapper = userMapper;
        this.coinMapper = coinMapper;
    }

    @Override
    public Notification toEntity(NotificationDto notificationDto) {
        return new Notification(
                userMapper.toEntity(notificationDto.getUser()),
                notificationDto.getMessageId(),
                notificationDto.getChatId(),
                coinMapper.toEntity(notificationDto.getCoin()),
                notificationDto.isRecurring(),
                notificationDto.isTriggered(),
                notificationDto.getInitialPrice(),
                notificationDto.getTargetPercentage(),
                notificationDto.getDirection()
        );
    }

    @Override
    public NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                userMapper.toDto(notification.getUser()),
                notification.getMessageId(),
                notification.getChatId(),
                coinMapper.toDto(notification.getCoin()),
                notification.isRecurring(),
                notification.isTriggered(),
                notification.getInitialPrice(),
                notification.getTargetPercentage(),
                notification.getDirection()
        );
    }

    public Notification toEntity(NotificationDto dto, User user, Coin coin) {
        return new Notification(
                user,
                dto.getMessageId(),
                dto.getChatId(),
                coin,
                dto.isRecurring(),
                dto.isTriggered(),
                dto.getInitialPrice(),
                dto.getTargetPercentage(),
                dto.getDirection()
        );
    }
}