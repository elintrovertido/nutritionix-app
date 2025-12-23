package com.nutritionix.authentication_service.mapper;

import com.nutritionix.authentication_service.dto.RegisterRequest;
import com.nutritionix.authentication_service.model.AuthUser;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;


@Component
public class AuthUserMapper {

    public AuthUser registerToAuthUserMapper(@Valid RegisterRequest registerRequest){
        return AuthUser.builder()
                .userName(registerRequest.getUserName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .roles(registerRequest.getRoles())
                .build();
    }
}
