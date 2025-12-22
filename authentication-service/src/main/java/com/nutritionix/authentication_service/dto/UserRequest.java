package com.nutritionix.authentication_service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @Email
    @NotBlank
    private String email;

    @Size(min=8)
    @NotBlank
    private String password;
}
