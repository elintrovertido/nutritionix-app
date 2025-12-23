package com.nutritionix.authentication_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "first name is required")
    private String firstName;

    @NotEmpty(message = "last name is required")
    private String lastName;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "Date of Birth is required")
    private LocalDate dob;

    @NotEmpty(message = "user name is required")
    private String userName;

    @Email
    private String email;

    @NotEmpty(message = "password is required")
    @Min(8)
    private String password;

    @NotEmpty(message = "role is required")
    private String roles;

}
