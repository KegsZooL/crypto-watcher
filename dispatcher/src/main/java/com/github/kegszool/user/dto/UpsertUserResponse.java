package com.github.kegszool.user.dto;

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