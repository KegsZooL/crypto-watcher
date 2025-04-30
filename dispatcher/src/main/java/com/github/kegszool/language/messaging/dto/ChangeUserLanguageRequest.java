package com.github.kegszool.language.messaging.dto;

import com.github.kegszool.user.messaging.dto.UserDto;

public record ChangeUserLanguageRequest(UserDto user, String language) { }