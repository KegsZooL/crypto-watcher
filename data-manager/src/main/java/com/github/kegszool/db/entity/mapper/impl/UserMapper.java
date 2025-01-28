package com.github.kegszool.db.entity.mapper.impl;

import com.github.kegszool.db.entity.base.User;
import com.github.kegszool.db.entity.mapper.EntityMapper;
import com.github.kegszool.db.entity.dto.impl.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends EntityMapper<User, UserDto> {

    @Override
    public User toEntity(UserDto userDto) {
        return new User(
            userDto.getId(),
            userDto.getTelegramId(),
            userDto.getFirstName(),
            userDto.getLastName()
        );
    }

    @Override
    public UserDto toDto(User user) {
        return new UserDto(
            user.getId(),
            user.getTelegramId(),
            user.getFirstName(),
            user.getLastName()
        );
    }
}