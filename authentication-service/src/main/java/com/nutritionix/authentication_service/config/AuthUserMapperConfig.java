package com.nutritionix.authentication_service.config;


import com.nutritionix.authentication_service.mapper.AuthUserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthUserMapperConfig {

    @Bean
    public AuthUserMapper authUserMapper(){
        return new AuthUserMapper();
    }
}
