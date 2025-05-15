package com.github.kegszool.notification.update;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.github.kegszool.user.messaging.dto.UserData;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationUpdateDto {
    private String chatId;
    private UserData userData;
}