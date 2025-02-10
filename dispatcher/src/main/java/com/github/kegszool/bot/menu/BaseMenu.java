package com.github.kegszool.bot.menu;

import com.github.kegszool.exception.bot.menu.MenuException;
import com.github.kegszool.exception.bot.menu.configuration.sections.parsing.InvalidKeyValuePairException;
import com.github.kegszool.exception.bot.menu.configuration.sections.InvalidMenuSectionConfigException;
import com.github.kegszool.utils.KeyboardFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
public abstract class BaseMenu implements Menu {

    protected final Map<String, String> SECTIONS = new LinkedHashMap<>();
    protected InlineKeyboardMarkup menuKeyboard;

    @PostConstruct
    protected void initializeMenu() throws MenuException {
        String SECTIONS_CONFIG = getSectionsConfig();
        int maxButtons = getMaxButtonsPerRow();
        try {
            if (SECTIONS_CONFIG != null && !SECTIONS_CONFIG.isEmpty()) {
                parseSectionConfig(SECTIONS_CONFIG);
                menuKeyboard = KeyboardFactory.create(SECTIONS, maxButtons);
            } else {
                handleMenuException(
                    "Config is empty or null: " + SECTIONS_CONFIG,
                    new InvalidMenuSectionConfigException("Config: " + SECTIONS_CONFIG)
                );
            }
        } catch (Exception ex) {
            handleMenuException("Error during menu initialization!", ex);
        }
    }

    protected abstract String getSectionsConfig();

    protected abstract int getMaxButtonsPerRow();

    private void parseSectionConfig(String menuSectionsConfig) throws MenuException {
        try {
            String[] pairs = menuSectionsConfig.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    SECTIONS.put(keyValue[0], keyValue[1]);
                } else {
                    handleMenuException(
                            "Incorrect key-value format: \"" + pair + "\" in config: " + menuSectionsConfig,
                            new InvalidKeyValuePairException("Invalid key-value pair: " + pair)
                    );
                }
            }
        } catch (Exception ex) {
            handleMenuException("Error parsing menu sections config: " + menuSectionsConfig, ex);
        }
    }

    private void handleMenuException(String logMessage, Exception ex) throws MenuException {
        log.error(logMessage, ex);
        if (ex instanceof MenuException) {
            throw (MenuException) ex;
        } else {
            throw new MenuException(logMessage, ex);
        }
    }
}