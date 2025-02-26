package com.github.kegszool.bot.menu.service.section.builder;

import com.github.kegszool.messaging.dto.database_entity.UserData;

public interface SectionBuilder {
    String buildSectionsConfig(UserData userData);
}