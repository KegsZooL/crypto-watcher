package com.github.kegszool.coin.selection.command;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.menu.base.main.command.desc.BaseMenuCommandDescription;

@Component
public class CoinSectionsEditorMenuCommandDescription extends BaseMenuCommandDescription {

    public CoinSectionsEditorMenuCommandDescription(
            @Value("${menu.edit_coin_sections.command}") String command,
            @Value("${menu.edit_coin_sections.command_description.ru}") String descriptionRu,
            @Value("${menu.edit_coin_sections.command_description.en}") String descriptionEn
    ) {
        super(command, Map.of(
                "ru", descriptionRu,
                "en", descriptionEn
            )
        );
    }
}