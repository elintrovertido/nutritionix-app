package com.nutritionix.userservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(long id,
                           String firstName,
                           String lastName,
                           String email,
                           LocalDate dob,
                           String role,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
}
