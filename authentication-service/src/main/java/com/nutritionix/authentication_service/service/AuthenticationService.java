package com.nutritionix.authentication_service.service;

import com.nutritionix.authentication_service.dto.RegisterRequest;
import com.nutritionix.authentication_service.dto.RegisterResponse;
import com.nutritionix.authentication_service.exception.DataProcessingException;
import com.nutritionix.authentication_service.exception.UserAlreadyExistException;
import com.nutritionix.authentication_service.mapper.AuthUserMapper;
import com.nutritionix.authentication_service.model.AuthUser;
import com.nutritionix.authentication_service.repository.AuthenticationRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private final AuthUserMapper authUserMapper;
    private final AuthenticationRepository authenticationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AuthenticationService(KafkaTemplate<String, String> kafkaTemplate, AuthUserMapper authUserMapper, AuthenticationRepository authenticationRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.authUserMapper = authUserMapper;
        this.authenticationRepository = authenticationRepository;
    }


    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        try {
            if (authenticationRepository.existsByEmail(registerRequest.getEmail())) {
                throw new UserAlreadyExistException("User Already Exists with Email : " + registerRequest.getEmail());
            }

            if (authenticationRepository.existsByUserName(registerRequest.getUserName())) {
                throw new UserAlreadyExistException("User Already Exists with UserName : " + registerRequest.getUserName());
            }

            AuthUser authUser = saveAuthUser(registerRequest);

            return RegisterResponse.builder().email(authUser.getEmail()).userName(authUser.getUserName()).message("User Registered").createdTimeStamp(LocalDateTime.now()).build();

        } catch (UserAlreadyExistException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataProcessingException("Database error occurred while creating user" + ex.getMessage());
        }
    }

    private AuthUser saveAuthUser(RegisterRequest registerRequest) {
        AuthUser authUser = authUserMapper.registerToAuthUserMapper(registerRequest);
        authUser.setEnabled(true);
        authenticationRepository.save(authUser);
        return authUser;
    }


}
