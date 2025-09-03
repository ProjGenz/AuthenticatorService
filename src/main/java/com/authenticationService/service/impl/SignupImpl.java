package com.authenticationService.service.impl;

import com.authenticationService.model.dao.User;
import com.authenticationService.repository.UserRepository;
import com.authenticationService.service.Signup;
import com.authenticationService.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SignupImpl implements Signup {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SignupImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public SignupImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void register(String email, String password, String username, HttpServletResponse response) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists with this email!");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists with this username!");
        }

        // ✅ Hash password before saving
        String hashedPassword = passwordEncoder.encode(password);

        // ✅ Save user in DB
        User user = new User(email, username, hashedPassword);
        userRepository.save(user);

        // ✅ Generate JWT with claims
        String token = jwtUtil.generateTokenWithClaims(user);

        // ✅ Create secure cookie (using helper)
        Cookie cookie = jwtUtil.createJwtCookie(token);

        // ⚠️ In dev, you may keep Secure=false, but in prod set Secure=true (only works on HTTPS)
        cookie.setSecure(false);

        response.addCookie(cookie);

        LOGGER.info("User registered successfully with email: {}", email);
    }

    @Override
    public long getRemainingSessionTime(String email) {
        // For now, fixed session time of 20 days
        return Duration.ofDays(20).getSeconds();
    }
}
