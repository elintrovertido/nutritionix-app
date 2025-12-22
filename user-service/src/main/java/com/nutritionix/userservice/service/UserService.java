package com.nutritionix.userservice.service;

import com.nutritionix.userservice.dto.UserRequest;
import com.nutritionix.userservice.dto.UserResponse;
import com.nutritionix.userservice.exception.DataProcessingException;
import com.nutritionix.userservice.exception.UserAlreadyExistException;
import com.nutritionix.userservice.exception.UserNotFoundException;
import com.nutritionix.userservice.mapper.UserMapper;
import com.nutritionix.userservice.model.User;
import com.nutritionix.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest userRequest) {
        log.info("Attempting to create new user with email: {}", userRequest.getEmail());

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            log.warn("User creation failed — email '{}' already exists.", userRequest.getEmail());
            throw new UserAlreadyExistException("User with email '" + userRequest.getEmail() + "' already exists.");
        }

        try {
            User user = UserMapper.userRequestToUser(userRequest);
            User savedUser = userRepository.save(user);
            log.info("User created successfully with ID: {}", savedUser.getId());
            return UserMapper.userToUserResponse(savedUser);
        } catch (DataAccessException ex) {
            log.error("Database error while creating user with email {}: {}", userRequest.getEmail(), ex.getMessage(), ex);
            throw new DataProcessingException("Database error occurred while creating user");
        }
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        log.debug("Fetching all users from database...");
        List<User> users = userRepository.findAll();
        log.info("Total users fetched: {}", users.size());
        return users.stream().map(UserMapper::userToUserResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(long id) {
        log.debug("Fetching user by ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found with ID: {}", id);
            return new UserNotFoundException("User not found with ID: " + id);
        });

        log.info("User retrieved successfully with ID: {}", id);
        return UserMapper.userToUserResponse(user);
    }

    public UserResponse updateUser(long id, UserRequest userRequest) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("Update failed — user not found with ID: {}", id);
            return new UserNotFoundException("User not found with ID: " + id);
        });

        boolean isUpdated = false;

        if (!user.getFirstName().equals(userRequest.getFirstName())) {
            user.setFirstName(userRequest.getFirstName());
            isUpdated = true;
        }

        if (!user.getLastName().equals(userRequest.getLastName())) {
            user.setLastName(userRequest.getLastName());
            isUpdated = true;
        }

        if (!user.getDob().equals(userRequest.getDob())) {
            user.setDob(userRequest.getDob());
            isUpdated = true;
        }

        if (!user.getEmail().equals(userRequest.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                log.warn("Update failed — email '{}' already exists for another user.", userRequest.getEmail());
                throw new UserAlreadyExistException("User with email '" + userRequest.getEmail() + "' already exists.");
            }
            user.setEmail(userRequest.getEmail());
            isUpdated = true;
        }

        if (!isUpdated) {
            log.info("No changes detected for user ID: {}", id);
            return UserMapper.userToUserResponse(user);
        }

        try {
            User updatedUser = userRepository.save(user);
            log.info("User updated successfully with ID: {}", updatedUser.getId());
            return UserMapper.userToUserResponse(updatedUser);
        } catch (Exception ex) {
            log.error("Database error while updating user with ID {}: {}", id, ex.getMessage(), ex);
            throw new DataProcessingException("Database error occurred while updating user");
        }
    }

    public UserResponse deleteUser(long id) {
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found"));
            userRepository.delete(user);
            return UserMapper.userToUserResponse(user);
        }catch(Exception ex){
            log.error("Unexpected error occurred while deleting user {}", ex.getMessage());
            throw new DataProcessingException("Unexpected error occurred while deleting user : " + id);
        }

    }

    public List<UserResponse> getUsersByRole(String role){
        List<User> users = userRepository.findAllByRole(role);
        return users.stream().map(UserMapper::userToUserResponse).toList();
    }

}
