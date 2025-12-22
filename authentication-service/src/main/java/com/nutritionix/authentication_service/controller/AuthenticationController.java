package com.nutritionix.authentication_service.controller;

import com.nutritionix.authentication_service.dto.UserRequest;
import com.nutritionix.authentication_service.dto.UserResponse;
import com.nutritionix.authentication_service.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> authenticateUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = authenticationService.authenticateUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.FOUND);
    }

    @PostMapping("/post")
    public ResponseEntity<String> testKafka(@RequestBody String str) {
        String userResponse = authenticationService.testKafka(str);
        return new ResponseEntity<>(userResponse, HttpStatus.FOUND);
    }

}
