package com.nutritionix.authentication_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritionix.authentication_service.dto.*;
import com.nutritionix.authentication_service.exception.DataProcessingException;
import com.nutritionix.authentication_service.exception.InvalidCredentialsException;
import com.nutritionix.authentication_service.exception.UserAlreadyExistException;
import com.nutritionix.authentication_service.mapper.AuthUserMapper;
import com.nutritionix.authentication_service.model.AuthUser;
import com.nutritionix.authentication_service.repository.AuthenticationRepository;
import com.nutritionix.authentication_service.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {

    private final AuthUserMapper authUserMapper;
    private final AuthenticationRepository authenticationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        log.info("Entered into Register User : {}", registerRequest.getUserName());
        try {
            if (authenticationRepository.existsByEmail(registerRequest.getEmail())) {
                log.error("User Already Exists with Email : {}", registerRequest.getEmail());
                throw new UserAlreadyExistException("User Already Exists with Email : " + registerRequest.getEmail());
            }

            if (authenticationRepository.existsByUserName(registerRequest.getUserName())) {
                log.error("User Already Exists with UserName  : {}", registerRequest.getUserName());
                throw new UserAlreadyExistException("User Already Exists with UserName : " + registerRequest.getUserName());
            }


            AuthUser authUser = saveAuthUser(registerRequest);
            String userEvent = objectMapper.writeValueAsString(registerRequest);
            kafkaTemplate.send(Constants.USER_REGISTERED_TOPIC, userEvent);

            return RegisterResponse.builder()
                    .email(authUser.getEmail())
                    .userName(authUser.getUsername())
                    .message("User Registered")
                    .createdTimeStamp(LocalDateTime.now()).build();
        } catch (UserAlreadyExistException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Database error occurred while creating user : {}", registerRequest.getUserName());
            throw new DataProcessingException("Database error occurred while creating user" + ex.getMessage());
        }
    }

    private AuthUser saveAuthUser(RegisterRequest registerRequest) {
        AuthUser authUser = authUserMapper.registerToAuthUserMapper(registerRequest);
        authUser.setEnabled(true);
        authUser.setPassword(passwordEncoder.encode(authUser.getPassword()));
        authenticationRepository.save(authUser);
        log.info("User Saved : {}", authUser.getUsername());
        return authUser;
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );
            AuthUser authUser =(AuthUser) authenticate.getPrincipal();

            String token = jwtService.generateToken(authUser);
            return LoginResponse.builder()
                    .userName(loginRequest.getUserName())
                    .accessToken(token)
                    .expiresIn(jwtService.getExpiration(token))
                    .build();
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException(ex.getMessage());
        } catch (Exception ex) {
            throw new DataProcessingException("Error occurred : " + ex.getMessage());
        }
    }

    public List<AuthUserDTO> getUsers() {
        List<AuthUser> authUsers = authenticationRepository.findAll();
        return authUsers.stream()
                .map(user -> {
                    return AuthUserDTO.builder()
                            .id(user.getId())
                            .userName(user.getUsername())
                            .email(user.getEmail())
                            .createdAt(user.getCreatedAt())
                            .enabled(user.isEnabled())
                            .roles(user.getRoles())
                            .build();
                }).toList();
    }
}