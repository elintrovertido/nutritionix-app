package com.nutritionix.userservice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritionix.userservice.dto.UserRequest;
import com.nutritionix.userservice.dto.UserResponse;
import com.nutritionix.userservice.events.UserRegisterEvent;
import com.nutritionix.userservice.mapper.UserMapper;
import com.nutritionix.userservice.service.UserService;
import com.nutritionix.userservice.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class UserEventConsumer {

    private final ObjectMapper objectMapper;

    private final UserService userService;

    public UserEventConsumer(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @KafkaListener(topics = Constants.USER_REGISTERED_TOPIC, groupId = "user-service-group")
    public void consumeUserRegisteredEvent(String message) throws JsonProcessingException {
        log.info(message);
        UserRegisterEvent userRegisterEvent = objectMapper.readValue(message, UserRegisterEvent.class);
        UserRequest userRequest = UserMapper.userRegisterEventToUserRequest(userRegisterEvent);
        UserResponse user = userService.createUser(userRequest);
        log.info("Created User with ID : {}", user.id());
    }

}
