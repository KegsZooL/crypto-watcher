package com.github.kegszool.menu.service;

import com.github.kegszool.user.dto.UserData;
import com.github.kegszool.user.menu.UserDataDependentMenu;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class MenuUpdaterService {

    private final Map<String, UserDataDependentMenu> userDataDependentMenus = new ConcurrentHashMap<>();

    public void registerUserDataDependentMenu(String menuName, UserDataDependentMenu menu) {
        userDataDependentMenus.put(menuName, menu);
    }

    public void updateMenus(UserData userData) {
        userDataDependentMenus.values().forEach(menu -> menu.updateMenu(userData));
    }
}