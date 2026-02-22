package com.nutritionix.authentication_service.config;

import com.nutritionix.authentication_service.repository.AuthenticationRepository;
import com.nutritionix.authentication_service.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         return http.csrf(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(auth ->
                         auth
                                 .requestMatchers( "/auth/register").permitAll()
                                 .requestMatchers("/auth/login").permitAll()
                                 .anyRequest().authenticated()
                 )
                 .httpBasic(Customizer.withDefaults())
                 .build();
    }


    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder(){
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean("userDetailsService")
    public UserDetailsService userDetailsService(AuthenticationRepository authenticationRepository){
        return new CustomUserDetailsService(authenticationRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(@Qualifier("userDetailsService") UserDetailsService customUserDetailsService,
                                                       @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder
    ){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }
}
