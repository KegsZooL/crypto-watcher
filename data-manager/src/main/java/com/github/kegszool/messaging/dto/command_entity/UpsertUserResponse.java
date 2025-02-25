package com.github.kegszool.messaging.dto.command_entity;

import com.github.kegszool.messaging.dto.database_entity.UserData;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpsertUserResponse {
    private boolean success;
    private UserData userData;
}
