package com.github.kegszool.menu.util;

import com.github.kegszool.user.dto.UserData;

public interface SectionBuilder {
    String buildSectionsConfig(UserData userData);
}