package com.nutritionix.userservice.controller;

import com.nutritionix.userservice.dto.UserRequest;
import com.nutritionix.userservice.dto.UserResponse;
import com.nutritionix.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse user = userService.createUser(userRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> userResponseList = userService.getUsers();
        return new ResponseEntity<>(userResponseList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") long id) {
        UserResponse userResponse = userService.getUser(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") long id, @Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.updateUser(id, userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") long id) {
        UserResponse userResponse = userService.deleteUser(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping(params = "role")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@RequestParam String role) {
        List<UserResponse> userResponseList = userService.getUsersByRole(role);
        return new ResponseEntity<>(userResponseList, HttpStatus.OK);
    }


}
