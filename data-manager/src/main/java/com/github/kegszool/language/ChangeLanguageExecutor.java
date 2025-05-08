package com.github.kegszool.language;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import com.github.kegszool.user.UserDataBuilder;
import org.springframework.beans.factory.annotation.Value;

import com.github.kegszool.database.entity.base.User;
import com.github.kegszool.database.entity.base.UserPreference;
import com.github.kegszool.database.entity.service.impl.UserService;
import com.github.kegszool.database.repository.impl.UserPreferenceRepository;

import com.github.kegszool.messaging.RequestExecutor;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.dto.database_entity.UserDto;
import com.github.kegszool.messaging.dto.database_entity.UserData;
import com.github.kegszool.messaging.dto.command_entity.ChangeUserLanguageRequest;

@Service
public class ChangeLanguageExecutor implements RequestExecutor<ChangeUserLanguageRequest, UserData> {

    private final String routingKey;
    private final UserService userService;
    private final UserDataBuilder userDataBuilder;
    private final UserPreferenceRepository userPreferenceRepository;

    public ChangeLanguageExecutor(
            @Value("${spring.rabbitmq.template.routing-key.change_language.response}") String routingKey,
            UserService userService,
            UserPreferenceRepository userPreferenceRepository,
            UserDataBuilder userDataBuilder
    ) {
        this.routingKey = routingKey;
        this.userDataBuilder =  userDataBuilder;
        this.userService = userService;
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Override
    public ServiceMessage<UserData> execute(ServiceMessage<ChangeUserLanguageRequest> serviceMessage) {

        ChangeUserLanguageRequest request = serviceMessage.getData();
        UserDto userDto = request.user();
        String newLanguage = request.language();

        Pair<Boolean, User> userResult = userService.findOrCreate(userDto);
        User user = userResult.getSecond();

        UserPreference preference = userPreferenceRepository.findById(user.getId())
                .orElseGet(() -> {
                    UserPreference newPreference = new UserPreference(user, newLanguage);
                    return userPreferenceRepository.save(newPreference);
                });

        preference.setInterfaceLanguage(newLanguage);
        userPreferenceRepository.save(preference);

        UserData userData =userDataBuilder.buildUserData(user);
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
