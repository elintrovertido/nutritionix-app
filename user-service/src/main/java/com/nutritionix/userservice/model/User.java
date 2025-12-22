package com.nutritionix.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", length = 50, nullable = false)
    @NotEmpty(message = "first name is required")
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    @NotEmpty(message = "last name is required")
    private String lastName;

    @Column(name = "dob", nullable = false)
    @NotNull(message = "Date of Birth is required")
    private LocalDate dob;

    @Column(name = "email", unique = true, nullable = false)
    @NotEmpty
    @Email
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Column(name = "role", nullable = false)
    @NotEmpty
    private String role;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
