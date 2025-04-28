package com.github.kegszool.messaging.dto.command_entity;

import com.github.kegszool.messaging.dto.database_entity.UserDto;

public record ChangeUserLanguageRequest(UserDto user, String language) { }