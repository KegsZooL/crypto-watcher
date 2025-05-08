package com.github.kegszool.user;

import com.github.kegszool.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long telegramId) {
        super("Telegram id: " + telegramId);
    }
}