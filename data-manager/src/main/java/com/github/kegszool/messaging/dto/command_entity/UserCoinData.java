package com.github.kegszool.messaging.dto.command_entity;

import com.github.kegszool.messaging.dto.database_entity.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCoinData {
    private UserDto user;
    private List<String> coinsToAdd;
}