package com.nutritionix.authentication_service.service;

import com.nutritionix.authentication_service.dto.LoginRequest;
import com.nutritionix.authentication_service.dto.LoginResponse;
import com.nutritionix.authentication_service.dto.RegisterRequest;
import com.nutritionix.authentication_service.dto.RegisterResponse;
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
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {

    private final AuthUserMapper authUserMapper;
    private final AuthenticationRepository authenticationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        try {
            if (authenticationRepository.existsByEmail(registerRequest.getEmail())) {
                throw new UserAlreadyExistException("User Already Exists with Email : " + registerRequest.getEmail());
            }

            if (authenticationRepository.existsByUserName(registerRequest.getUserName())) {
                throw new UserAlreadyExistException("User Already Exists with UserName : " + registerRequest.getUserName());
            }

            AuthUser authUser = saveAuthUser(registerRequest);
            String userEvent = objectMapper.writeValueAsString(registerRequest);
            kafkaTemplate.send(Constants.USER_REGISTERED_TOPIC, userEvent);

            return RegisterResponse.builder()
                    .email(authUser.getEmail())
                    .userName(authUser.getUserName())
                    .message("User Registered")
                    .createdTimeStamp(LocalDateTime.now()).build();
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

    public LoginResponse loginUser(LoginRequest loginRequest) {
        try {
            AuthUser user = authenticationRepository.findByUserNameOrEmail(loginRequest.getUserName(), loginRequest.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("User doesn't exist"));
            if (!user.getPassword().equals(loginRequest.getPassword())) {
                throw new InvalidCredentialsException("Invalid Credentials, Password doesn't match");
            }
            String token = jwtService.generateToken(user);
            return LoginResponse.builder()
                    .userName(user.getUserName())
                    .accessToken(token)
                    .expiresIn(jwtService.getExpiration(token))
                    .build();
        } catch(InvalidCredentialsException ex){
            throw ex;
        }
        catch (Exception ex) {
            throw new DataProcessingException("Error occurred : " + ex.getMessage());
        }
    }


}