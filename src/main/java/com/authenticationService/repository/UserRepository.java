package com.authenticationService.repository;

import com.authenticationService.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {
    // Define custom query methods if needed
    // For example, to find a user by email:
    // Optional<User> findByEmail(String email);

    public UUID findUserIdByEmailId(String emailId);
}
