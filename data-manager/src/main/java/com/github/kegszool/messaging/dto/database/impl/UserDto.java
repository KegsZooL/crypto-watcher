package com.github.kegszool.messaging.dto.database.impl;

import com.github.kegszool.messaging.dto.database.BaseEntityDto;

import java.math.BigInteger;

public class UserDto extends BaseEntityDto {

    private BigInteger telegramId;
    private String firstName, lastName;

    public UserDto(int id, BigInteger telegramId, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.telegramId = telegramId;
    }

    public void setTelegramId(BigInteger telegramId) {
        this.telegramId = telegramId;
    }

    public BigInteger getTelegramId() {
        return telegramId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
}