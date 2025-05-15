package com.github.kegszool.database.entity.mapper.impl;

import org.springframework.stereotype.Component;
import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.mapper.EntityMapper;
import com.github.kegszool.messaging.dto.database_entity.UserDto;

@Component
public class UserMapper extends EntityMapper<User, UserDto> {

    @Override
    public User toEntity(UserDto userDto) {
        return new User(
            userDto.getTelegramId(),
            userDto.getFirstName(),
            userDto.getLastName()
        );
    }

    @Override
    public UserDto toDto(User user) {
        return new UserDto(
            user.getTelegramId(),
            user.getFirstName(),
            user.getLastName()
        );
    }
}