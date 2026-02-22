package com.nutritionix.authentication_service.repository;

import com.nutritionix.authentication_service.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    Optional<AuthUser> findByUserNameOrEmail(String userName, String email);

}
