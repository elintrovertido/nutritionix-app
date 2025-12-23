package com.nutritionix.authentication_service.controller;

import com.nutritionix.authentication_service.dto.RegisterRequest;
import com.nutritionix.authentication_service.dto.RegisterResponse;
import com.nutritionix.authentication_service.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest){
        RegisterResponse registerResponse = authenticationService.registerUser(registerRequest);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }




}
