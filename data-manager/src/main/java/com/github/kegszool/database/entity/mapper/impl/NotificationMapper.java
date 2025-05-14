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
    public Notification toEntity(NotificationDto dto) {
        return new Notification(
                userMapper.toEntity(dto.getUser()),
                dto.getMessageId(),
                dto.getChatId(),
                coinMapper.toEntity(dto.getCoin()),
                dto.isRecurring(),
                dto.isTriggered(),
                dto.getInitialPrice(),
                dto.getTriggeredPrice(),
                dto.getTargetPercentage(),
                dto.getDirection(),
                dto.getLastTriggeredTime()
        );
    }

    @Override
    public NotificationDto toDto(Notification entity) {
        return new NotificationDto(
                userMapper.toDto(entity.getUser()),
                entity.getMessageId(),
                entity.getChatId(),
                coinMapper.toDto(entity.getCoin()),
                entity.isRecurring(),
                entity.isTriggered(),
                entity.getInitialPrice(),
                entity.getTriggeredPrice(),
                entity.getTargetPercentage(),
                entity.getDirection(),
                entity.getLastTriggeredTime()
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
                dto.getTriggeredPrice(),
                dto.getTargetPercentage(),
                dto.getDirection(),
                dto.getLastTriggeredTime()
        );
    }
}