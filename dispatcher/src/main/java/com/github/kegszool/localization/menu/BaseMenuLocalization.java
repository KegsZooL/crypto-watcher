package com.github.kegszool.localization.menu;

import java.util.Map;
import lombok.Getter;
import jakarta.annotation.Nullable;

@Getter
public abstract class BaseMenuLocalization {

    protected final String name;
    protected final Map<String, String> titles;
    protected final Map<String, String> sectionsConfig;
    protected final Map<String, Map<String, String>> answerMessages;

    public BaseMenuLocalization(
            String name,
            Map<String, String> titles,
            Map<String, String> sectionsConfig,
            @Nullable Map<String, Map<String, String>> answerMessages
    ) {
        this.name = name;
        this.titles = titles;
        this.sectionsConfig = sectionsConfig;
        this.answerMessages = answerMessages;
    }
}
