package com.nutritionix.authentication_service.service;

import com.nutritionix.authentication_service.dto.UserRequest;
import com.nutritionix.authentication_service.dto.UserResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public AuthenticationService(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public UserResponse authenticateUser(UserRequest userRequest) {
        return null;
    }

    public String testKafka(String str) {
        kafkaTemplate.send("test-topic", str);
        return "sent";
    }

}
