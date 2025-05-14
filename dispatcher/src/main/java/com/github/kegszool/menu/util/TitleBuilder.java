package com.github.kegszool.menu.util;

import com.github.kegszool.user.messaging.dto.UserData;

public interface TitleBuilder {
    String buildTitle(UserData userData, String language);
    String getDefaultTitle();
}