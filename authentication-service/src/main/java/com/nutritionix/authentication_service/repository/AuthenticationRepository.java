package com.nutritionix.authentication_service.repository;

import com.nutritionix.authentication_service.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<AuthUser, Long> {

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}
