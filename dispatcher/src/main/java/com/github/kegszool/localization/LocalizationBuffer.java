package com.github.kegszool.localization;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;

import com.github.kegszool.localization.exception.MenuLocalizationNotFoundException;

@Log4j2
@Component
public class LocalizationBuffer {

    private final Map<String, BaseMenuLocalization> menuNameToLocalization = new HashMap<>();

    @Autowired
    public LocalizationBuffer(List<BaseMenuLocalization> localizations) {
        localizations.forEach(l ->
                menuNameToLocalization.put(l.getName(), l));
    }

    public BaseMenuLocalization get(String menuName) {
        return Optional.ofNullable(menuNameToLocalization.get(menuName))
                .orElseThrow(() -> createException(menuName));
    }

    private MenuLocalizationNotFoundException createException(String key) {
        log.error("Menu localization with the current key '{}' was not found", key);
        return new MenuLocalizationNotFoundException("Key: " + key);
    }
}