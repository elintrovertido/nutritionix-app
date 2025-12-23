package com.nutritionix.authentication_service.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RegisterResponse {
    private String email;
    private String userName;
    private String message;
    private LocalDateTime createdTimeStamp;
}
