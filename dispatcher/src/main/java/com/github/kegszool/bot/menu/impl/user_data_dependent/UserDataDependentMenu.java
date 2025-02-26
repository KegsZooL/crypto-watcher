package com.github.kegszool.bot.menu.impl.user_data_dependent;

import com.github.kegszool.messaging.dto.database_entity.UserData;

public interface UserDataDependentMenu {
    void updateMenu(UserData user);
}