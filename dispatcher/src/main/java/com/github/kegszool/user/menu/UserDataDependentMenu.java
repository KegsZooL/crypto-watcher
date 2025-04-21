package com.github.kegszool.user.menu;

import com.github.kegszool.user.dto.UserData;

public interface UserDataDependentMenu {
    void updateMenu(UserData user);
}