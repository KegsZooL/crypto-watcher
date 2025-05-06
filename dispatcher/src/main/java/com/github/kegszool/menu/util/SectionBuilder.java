package com.github.kegszool.menu.util;

import com.github.kegszool.user.messaging.dto.UserData;

public interface SectionBuilder {
    String buildSectionsConfig(UserData userData, String locale);
}