package com.github.kegszool.coin.selection.command;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.github.kegszool.menu.base.main.command.desc.BaseMenuCommandDescription;

@Component
public class DisplayCoinSelectionMenuCommandDescription extends BaseMenuCommandDescription {

    public DisplayCoinSelectionMenuCommandDescription(
            @Value("${menu.coin_selection.command}") String command,
            @Value("${menu.coin_selection.command_description.ru}") String descriptionRu,
            @Value("${menu.coin_selection.command_description.en}") String descriptionEn
    ) {
        super(command, Map.of(
                "ru", descriptionRu,
                "en", descriptionEn
            )
    	);
    }
}