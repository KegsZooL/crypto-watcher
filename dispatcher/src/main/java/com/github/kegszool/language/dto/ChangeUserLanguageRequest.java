package com.github.kegszool.language.dto;

import com.github.kegszool.user.dto.UserDto;

public record ChangeUserLanguageRequest(UserDto user, String language) { }