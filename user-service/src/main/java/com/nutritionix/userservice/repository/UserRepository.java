package com.nutritionix.userservice.repository;

import com.nutritionix.userservice.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    List<User> findAllByRole(String role);


    @Query(value = "SELECT u FROM User u where u.status = :status")
    List<User> findByStatus(@Param("status") String status);

}
