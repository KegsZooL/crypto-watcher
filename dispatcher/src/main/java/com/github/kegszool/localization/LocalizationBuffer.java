package com.github.kegszool.localization;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kegszool.menu.base.main.command.desc.BaseMenuCommandDescription;
import com.github.kegszool.menu.exception.section.MenuCommandDescriptionNotFoundException;
import com.github.kegszool.localization.exception.MenuLocalizationNotFoundException;

@Log4j2
@Component
public class LocalizationBuffer {

    private final Map<String, BaseMenuLocalization> menuNameToLocalization = new HashMap<>();
    private final Map<String, BaseMenuCommandDescription> commandToDescription = new HashMap<>();

    @Autowired
    public LocalizationBuffer(List<BaseMenuLocalization> menus, List<BaseMenuCommandDescription> commands) {
        menus.forEach(m ->
                menuNameToLocalization.put(m.getName(), m));

        commands.forEach(c ->
                commandToDescription.put(c.getCommand(), c));
    }

    public BaseMenuLocalization getMenu(String menuName) {
        return Optional.ofNullable(menuNameToLocalization.get(menuName))
                .orElseThrow(() -> createMenuLocalizationException(menuName));
    }

    public BaseMenuCommandDescription getDescriptionForCommand(String command) {
        return Optional.ofNullable(commandToDescription.get(command))
                .orElseThrow(() -> createCommandDescriptionException(command));
    }

    private MenuLocalizationNotFoundException createMenuLocalizationException(String key) {
        log.error("Menu localization with the current key '{}' was not found", key);
        return new MenuLocalizationNotFoundException(key);
    }

    private MenuCommandDescriptionNotFoundException createCommandDescriptionException(String command) {
        log.error("Desc of the menu command '{}' was not found", command);
        return new MenuCommandDescriptionNotFoundException(command);
    }
}