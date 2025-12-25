package com.nutritionix.userservice.events;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class UserRegisterEvent {

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String userName;
    private String email;
    private String roles;

}
