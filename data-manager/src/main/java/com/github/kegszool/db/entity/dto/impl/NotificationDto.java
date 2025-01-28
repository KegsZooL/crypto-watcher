package com.github.kegszool.db.entity.dto.impl;

import com.github.kegszool.db.entity.base.Direction;
import com.github.kegszool.db.entity.dto.BaseEntityDto;

public class NotificationDto extends BaseEntityDto {

    private UserDto user;
    private CoinDto coin;

    private boolean isActive = true, isRecurring;
    private float targetPrice, targetPercentage;

    private Direction direction;

    public NotificationDto(
            int id,
            UserDto user,
            CoinDto coin,
            boolean isActive, boolean isRecurring,
            float targetPrice, float targetPercentage,
            Direction direction
    ) {
        this.id = id;
        this.user = user;
        this.coin = coin;
        this.isActive = isActive;
        this.isRecurring = isRecurring;
        this.targetPrice = targetPrice;
        this.targetPercentage = targetPercentage;
        this.direction = direction;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

    public void setCoin(CoinDto coin) {
        this.coin = coin;
    }

    public CoinDto getCoin() {
        return coin;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setTargetPrice(float targetPrice) {
        this.targetPrice = targetPrice;
    }

    public float getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPercentage(float targetPercentage) {
        this.targetPercentage = targetPercentage;
    }

    public float getTargetPercentage() {
        return targetPercentage;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}