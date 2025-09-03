package com.authenticationService.service;
import com.authenticationService.dto.LoginRequest;
import com.authenticationService.dto.LoginResponse;
import com.authenticationService.model.dao.User;
import com.authenticationService.repository.UserRepository;
import com.authenticationService.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class Loginimpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Optional<User> userOpt;

        // Check by email or username
        if (loginRequest.getIdentifier().contains("@")) {
            userOpt = userRepository.findByEmail(loginRequest.getIdentifier());
        } else {
            userOpt = userRepository.findByUsername(loginRequest.getIdentifier());
        }

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        User user = userOpt.get();

        // Validate password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        // Generate JWT
        String token = jwtUtil.generateTokenWithClaims(user);

        // Store in HTTP-only cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // only over HTTPS in production
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.ofDays(20).getSeconds());
        response.addCookie(cookie);

        // Return DTO instead of raw string
        return new LoginResponse("logged in successfully", user.getUsername(), user.getEmail());
    }

}
