package com.nutritionix.authentication_service.dto;

import com.nutritionix.authentication_service.utils.Roles;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AuthUserDTO {
    private long id;
    private String userName;
    private String email;
    private Roles roles;
    private boolean enabled;
    private LocalDate createdAt;
}
