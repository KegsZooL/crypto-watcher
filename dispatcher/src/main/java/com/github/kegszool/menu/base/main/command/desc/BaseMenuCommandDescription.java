package com.github.kegszool.menu.base.main.command.desc;

import java.util.Map;
import lombok.Getter;

@Getter
public abstract class BaseMenuCommandDescription {

    private final String command;
    private final Map<String, String> localeToDescription;

    public BaseMenuCommandDescription(String command, Map<String, String> localeToDescription) {
        this.command = command;
        this.localeToDescription = localeToDescription;
    }
}