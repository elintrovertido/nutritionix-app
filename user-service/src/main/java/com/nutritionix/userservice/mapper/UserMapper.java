package com.nutritionix.userservice.mapper;

import com.nutritionix.userservice.dto.UserRequest;
import com.nutritionix.userservice.dto.UserResponse;
import com.nutritionix.userservice.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User userRequestToUser(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .dob(userRequest.getDob())
                .role(userRequest.getRole())
                .build();
    }

    public static UserResponse userToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDob(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
