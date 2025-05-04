package com.github.kegszool.request.impl;

import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.base.UserPreference;
import com.github.kegszool.database.entity.service.impl.UserService;
import com.github.kegszool.database.repository.impl.UserPreferenceRepository;

import org.springframework.data.util.Pair;
import com.github.kegszool.request.RequestExecutor;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.database_entity.UserPreferenceDto;
import com.github.kegszool.messaging.dto.command_entity.ChangeUserLanguageRequest;
import org.springframework.stereotype.Service;


@Service
public class ChangeLanguageExecutor implements RequestExecutor<ChangeUserLanguageRequest, UserData> {

    private final String routingKey;
    private final UserService userService;
    private final UserPreferenceRepository userPreferenceRepository;

    public ChangeLanguageExecutor(
            @Value("${spring.rabbitmq.template.routing-key.change_language.response}") String routingKey,
            UserService userService,
            UserPreferenceRepository userPreferenceRepository
    ) {
        this.routingKey = routingKey;
        this.userService = userService;
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<ChangeUserLanguageRequest> serviceMessage) {

        ChangeUserLanguageRequest request = serviceMessage.getData();
        UserDto userDto = request.user();
        String newLanguage = request.language();

        Pair<Boolean, User> userResult = userService.findOrCreateWithPreferences(userDto);
        User user = userResult.getSecond();

        UserPreference preference = userPreferenceRepository.findById(user.getId())
                .orElseGet(() -> {
                    UserPreference newPreference = new UserPreference(user, newLanguage);
                    return userPreferenceRepository.save(newPreference);
                });

        preference.setInterfaceLanguage(newLanguage);
        userPreferenceRepository.save(preference);

        UserData userData = new UserData();
        userData.setUser(userDto);
        userData.setFavoriteCoins(userService.getUserFavoriteCoins(user.getId()));
        userData.setNotifications(userService.getUserNotifications(user.getId()));

        UserPreferenceDto userPreferenceDto = new UserPreferenceDto(userDto, newLanguage);
        userData.setUserPreference(userPreferenceDto);

        return new ServiceMessage<>(
                serviceMessage.getMessageId(),
                serviceMessage.getChatId(),
                userData
        );
    }

    @Override
    public String getResponseRoutingKey() {
        return routingKey;
    }
}
