package com.authenticationService.controller;

import com.authenticationService.dto.LoginRequest;
import com.authenticationService.dto.SignupRequest;
import com.authenticationService.service.AuthService;
import com.authenticationService.service.Signup;
import com.authenticationService.service.impl.SignupImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")  // base path for all auth APIs
public class AuthController {

    private static final String SUPPORTED_API_VERSION = "1.0.0";
    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final Signup signupService;
    private final AuthService authService;

    @Autowired
    public AuthController(Signup signupService, AuthService authService) {
        this.signupService = signupService;
        this.authService = authService;
    }

    // ✅ Register
    @PostMapping("/register")
    public String register(
            @RequestBody SignupRequest signupRequest,
            @RequestHeader(name = "api-version", required = false, defaultValue = SUPPORTED_API_VERSION) String apiVersion,
            HttpServletResponse response
    ) {
        if (!SUPPORTED_API_VERSION.equals(apiVersion)) {
            LOGGER.error("Unsupported API version: {}", apiVersion);
            throw new IllegalArgumentException("Unsupported API version: " + apiVersion);
        }

        signupService.register(
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                signupRequest.getUsername(),
                response
        );
        return "User registered successfully";
    }

    // ✅ Login
    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest loginRequest,
            @RequestHeader(name = "api-version", required = false, defaultValue = SUPPORTED_API_VERSION) String apiVersion,
            HttpServletResponse response
    ) {
        if (!SUPPORTED_API_VERSION.equals(apiVersion)) {
            LOGGER.error("Unsupported API version: {}", apiVersion);
            throw new IllegalArgumentException("Unsupported API version: " + apiVersion);
        }

        authService.login(loginRequest, response);
        return "Login successful";
    }

    // ✅ Get session time
    @GetMapping("/get-session-time")
    public long getRemainingSessionTime(
            @RequestParam(name = "email") String email,
            @RequestHeader(name = "api-version", required = false, defaultValue = SUPPORTED_API_VERSION) String apiVersion
    ) {
        if (!SUPPORTED_API_VERSION.equals(apiVersion)) {
            LOGGER.error("Unsupported API version: {}", apiVersion);
            throw new IllegalArgumentException("Unsupported API version: " + apiVersion);
        }

        return signupService.getRemainingSessionTime(email);
    }
}
