package com.github.kegszool.bot.menu.service;

import com.github.kegszool.exception.bot.menu.MenuException;
import com.github.kegszool.exception.bot.menu.configuration.sections.InvalidMenuSectionConfigException;
import com.github.kegszool.exception.bot.menu.configuration.sections.parsing.InvalidKeyValuePairException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Iterator;
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

    public void updateSections(LinkedHashMap<String, String> oldSections, String sectionsConfig, boolean saveActionButton) {
        LinkedHashMap<String, String> newSections = new LinkedHashMap<>();
        processSections(sectionsConfig, newSections);
        mergeSections(newSections, oldSections, saveActionButton);
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

    private void mergeSections(
            Map<String, String> sourceSections,
            Map<String, String> targetSections,
            boolean saveActionButton
    ) {
        Iterator<Map.Entry<String, String>> iterator = targetSections.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (!sourceSections.containsKey(entry.getKey()) && (!saveActionButton || !entry.getKey().startsWith(ACTION_PREFIX))) {
                iterator.remove();
            }
        }
        targetSections.putAll(sourceSections);
    }
}