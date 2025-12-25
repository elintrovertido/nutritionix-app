package com.nutritionix.authentication_service.controller;

import com.nutritionix.authentication_service.dto.LoginRequest;
import com.nutritionix.authentication_service.dto.LoginResponse;
import com.nutritionix.authentication_service.dto.RegisterRequest;
import com.nutritionix.authentication_service.dto.RegisterResponse;
import com.nutritionix.authentication_service.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = authenticationService.loginUser(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

}
