package com.github.kegszool.menu.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.localization.LocalizationService;
import com.github.kegszool.menu.exception.format.MenuKeyValueFormatException;
import com.github.kegszool.menu.exception.base.MenuException;

import java.util.Map;
import java.util.LinkedHashMap;

@Log4j2
@Service
public class MenuSectionService {

    private final String actionPrefix;
    private final LocalizationService localizationService;

    public MenuSectionService(
            @Value("${menu.action.prefix}") String actionPrefix,
            LocalizationService localizationService
    ) {
        this.actionPrefix = actionPrefix;
        this.localizationService = localizationService;
    }

    public LinkedHashMap<String, String> createSections(String sectionsConfig) {
        return new LinkedHashMap<>(parseSectionConfig(sectionsConfig));
    }

    public void update(LinkedHashMap<String, String> current, String sectionsConfig, boolean saveActionButton, String menuName) {
        LinkedHashMap<String, String> newSections = new LinkedHashMap<>();
        processSections(sectionsConfig, newSections);
        updateSectionsWithActionPriority(newSections, current,  saveActionButton, menuName);
    }

    private void processSections(String sectionsConfig, Map<String, String> sections) {
        try {
            if (sectionsConfig != null && !sectionsConfig.isEmpty()) {
            	sections.putAll(parseSectionConfig(sectionsConfig));
            }
        } catch (MenuException ex) {
            handleMenuException("Error during menu initialization!", ex);
        }
    }

    private Map<String, String> parseSectionConfig(String sectionsConfig) throws MenuException {
        Map<String, String> sections = new LinkedHashMap<>();
        try {
            String[] pairs = sectionsConfig.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    sections.put(keyValue[0], keyValue[1]);
                } else {
                    handleMenuException("Incorrect key-value format: \"" + pair + "\" in config: " + sectionsConfig,
                            new MenuKeyValueFormatException("Invalid key-value pair: " + pair)
                    );
                }
            }
        } catch (Exception ex) {
            handleMenuException("Error parsing menu sections config: " + sectionsConfig, ex);
        }
        return sections;
    }

    private void handleMenuException(String logMessage, Exception ex) throws MenuException {
        log.error(logMessage, ex);
        throw switch(ex) {
            case MenuException menuException -> menuException;
            default -> new MenuException(logMessage, ex);
        };
    }

    private void updateSectionsWithActionPriority(
            Map<String, String> sourceSections,
            Map<String, String> targetSections,
            boolean saveActionButton,
            String menuName
    ) {
        Map<String, String> result = new LinkedHashMap<>();

        sourceSections.forEach((key, value) -> {
            if (!(saveActionButton && key.startsWith(actionPrefix))) {
                result.put(key, value);
            }
        });

        Map<String, String> localizedSections = parseSectionConfig(localizationService.getSectionsConfig(menuName));

        if (saveActionButton) {
            targetSections.forEach((key, value) -> {
                if (key.startsWith(actionPrefix)) {
                    String localizedValue = localizedSections.getOrDefault(key, value);
                    result.put(key, localizedValue);
                }
            });
        }
        targetSections.clear();
        targetSections.putAll(result);
    }
}