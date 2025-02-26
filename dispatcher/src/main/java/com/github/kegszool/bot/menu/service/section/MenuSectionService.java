package com.github.kegszool.bot.menu.service.section;

import com.github.kegszool.exception.bot.menu.MenuException;
import com.github.kegszool.exception.bot.menu.configuration.section.InvalidMenuSectionConfigException;
import com.github.kegszool.exception.bot.menu.configuration.section.parsing.InvalidKeyValuePairException;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.LinkedHashMap;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MenuSectionService {

    @Value("${menu.action.prefix}")
    private String ACTION_PREFIX;

    public LinkedHashMap<String, String> createSections(String sectionsConfig) {
        return new LinkedHashMap<>(parseSectionConfig(sectionsConfig));
    }

    public void updateSections(LinkedHashMap<String, String> current, String sectionsConfig, boolean saveActionButton) {
        LinkedHashMap<String, String> newSections = new LinkedHashMap<>();
        processSections(sectionsConfig, newSections);
        updateSectionsWithActionPriority(newSections, current,  saveActionButton);
    }

    private void processSections(String sectionsConfig, Map<String, String> sections) {
        try {
            validateSectionsConfig(sectionsConfig);
            sections.putAll(parseSectionConfig(sectionsConfig));
        } catch (MenuException ex) {
            handleMenuException("Error during menu initialization!", ex);
        }
    }

    private void validateSectionsConfig(String sectionsConfig) {
        if (sectionsConfig == null || sectionsConfig.isEmpty()) {
            handleMenuException("Config is empty or null: " + sectionsConfig,
                    new InvalidMenuSectionConfigException("Config: " + sectionsConfig));
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
                            new InvalidKeyValuePairException("Invalid key-value pair: " + pair)
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

    private void updateSectionsWithActionPriority(Map<String, String> sourceSections, Map<String, String> targetSections, boolean saveActionButton) {
        Map<String, String> result = new LinkedHashMap<>();

        sourceSections.forEach((key, value) -> {
            if (!(saveActionButton && key.startsWith(ACTION_PREFIX))) {
                result.put(key, value);
            }
        });

        if (saveActionButton) {
            targetSections.forEach((key, value) -> {
                if (key.startsWith(ACTION_PREFIX)) {
                    result.put(key, value);
                }
            });
        }
        targetSections.clear();
        targetSections.putAll(result);
    }
}