package com.authenticationService.controller;

import com.authenticationService.dto.LoginRequest;
import com.authenticationService.dto.LoginResponse;
import com.authenticationService.dto.SignupRequest;
import com.authenticationService.service.Loginimpl;
import com.authenticationService.service.Signup;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")  // base path for all auth APIs
public class AuthController {

    private static final String SUPPORTED_API_VERSION = "1.0.0";
    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final Signup signupService;
    private final Loginimpl authService;

    @Autowired
    public AuthController(Signup signupService, Loginimpl authService) {
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
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest,
            @RequestHeader(name = "api-version", required = false, defaultValue = SUPPORTED_API_VERSION) String apiVersion,
            HttpServletResponse response
    ) {
        if (!SUPPORTED_API_VERSION.equals(apiVersion)) {
            throw new IllegalArgumentException("Unsupported API version: " + apiVersion);
        }

        return ResponseEntity.ok(authService.login(loginRequest, response));
    }
    @RestController
    @RequestMapping("/authenticator-service/test")
    public class TestController {

        @GetMapping("/secure")
        public ResponseEntity<String> secureEndpoint() {
            return ResponseEntity.ok("✅ JWT is valid, you can access this!");
        }
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
