package com.github.kegszool.bot.menu;

import com.github.kegszool.utils.KeyboardFactory;

import com.github.kegszool.exception.bot.menu.MenuException;
import com.github.kegszool.exception.bot.menu.configuration.sections.parsing.InvalidKeyValuePairException;
import com.github.kegszool.exception.bot.menu.configuration.sections.InvalidMenuSectionConfigException;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;

import lombok.extern.log4j.Log4j2;
import jakarta.annotation.PostConstruct;

@Log4j2
public abstract class BaseMenu implements Menu {

    @Autowired
    private KeyboardFactory keyboardFactory;

    protected final Map<String, String> SECTIONS = new LinkedHashMap<>();
    protected InlineKeyboardMarkup menuKeyboard;

    @PostConstruct
    protected void initializeMenu() throws MenuException {
        processSections(getSectionsConfig());
        menuKeyboard = keyboardFactory.create(SECTIONS, getMaxButtonsPerRow(), getFullWidthSections());
    }

    protected abstract String getSectionsConfig();

    private void processSections(String sectionsConfig) {
        try {
            validateSectionsConfig(sectionsConfig);
            parseSectionConfig(sectionsConfig);
        } catch (Exception ex) {
            handleMenuException("Error during menu initialization!", ex);
        }
    }

    private void validateSectionsConfig(String sectionsConfig) {
        if (sectionsConfig == null || sectionsConfig.isEmpty()) {
            handleMenuException("Config is empty or null: " + sectionsConfig,
                    new InvalidMenuSectionConfigException("Config: " + sectionsConfig));
        }
    }

    public void updateSections(String sectionsConfig) {
        if (sectionsConfig != null || sectionsConfig.equals(getSectionsConfig())) {
            return;
        }
        SECTIONS.clear();
        processSections(sectionsConfig);
        keyboardFactory.change(menuKeyboard, SECTIONS);
    }

    private void parseSectionConfig(String sectionsConfig) throws MenuException {
        try {
            String[] pairs = sectionsConfig.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    SECTIONS.put(keyValue[0], keyValue[1]);
                } else {
                    handleMenuException("Incorrect key-value format: \"" + pair + "\" in config: " + sectionsConfig,
                            new InvalidKeyValuePairException("Invalid key-value pair: " + pair)
                    );
                }
            }
        } catch (Exception ex) {
            handleMenuException("Error parsing menu sections config: " + sectionsConfig, ex);
        }
    }

    private void handleMenuException(String logMessage, Exception ex) throws MenuException {
        log.error(logMessage, ex);
        throw switch(ex) {
            case MenuException menuException -> menuException;
            default -> new MenuException(logMessage, ex);
        };
    }
    protected abstract int getMaxButtonsPerRow();
    protected abstract List<String> getFullWidthSections();

    @Override
    public InlineKeyboardMarkup getKeyboardMarkup() {
        return menuKeyboard;
    }
}