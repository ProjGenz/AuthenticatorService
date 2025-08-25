package com.authenticationService.service;
import com.authenticationService.dto.LoginRequest;
import com.authenticationService.model.dao.User;
import com.authenticationService.repository.UserRepository;
import com.authenticationService.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void login(LoginRequest loginRequest, HttpServletResponse response) {
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
        String token = jwtUtil.generateToken(user.getUsername());

        // Store in HTTP-only cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // only over HTTPS in production
        cookie.setPath("/");
        cookie.setMaxAge((int) (60 * 60 * 24 * 20)); // 20 days
        response.addCookie(cookie);
    }
}
