package com.nutritionix.authentication_service.controller;

import com.nutritionix.authentication_service.dto.*;
import com.nutritionix.authentication_service.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authenticationService.registerUser(registerRequest);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.loginUser(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users")
    public ResponseEntity<List<AuthUserDTO>> getUsersData() {
        List<AuthUserDTO> authUserDTOList = authenticationService.getUsers();
        return new ResponseEntity<>(authUserDTOList, HttpStatus.OK);
    }
}
