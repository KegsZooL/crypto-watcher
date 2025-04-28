package com.github.kegszool.database.entity.mapper.impl;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.database.entity.base.UserPreference;
import com.github.kegszool.database.entity.mapper.EntityMapper;
import com.github.kegszool.messaging.dto.database_entity.UserPreferenceDto;

@Component
public class UserPreferenceMapper extends EntityMapper<UserPreference, UserPreferenceDto> {

    private final UserMapper userMapper;

    @Autowired
    public UserPreferenceMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserPreference toEntity(UserPreferenceDto entityDto) {
        return new UserPreference(
                userMapper.toEntity(entityDto.user()),
                entityDto.interfaceLanguage()
        );
    }

    @Override
    public UserPreferenceDto toDto(UserPreference entity) {
        return new UserPreferenceDto(
                userMapper.toDto(entity.getUser()),
                entity.getInterfaceLanguage()
        );
    }
}